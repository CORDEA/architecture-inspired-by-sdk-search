package jp.cordea.sdksearcharchitecturedemo

import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class ColorSynchronizer @Inject constructor(
        private val remoteDataSource: ColorRemoteDataSource
) {
    val state: ReceiveChannel<ColorState> get() = mutableState.openSubscription()
    val result: ReceiveChannel<ColorResult> get() = mutableResult.openSubscription()

    private var cachedState = ColorState()
    private var cachedItem: ColorFetchResult.Success? = null
    private val mutableState = ConflatedBroadcastChannel<ColorState>()
    private val mutableResult = ConflatedBroadcastChannel<ColorResult>()

    fun sync(action: ColorAction) {
        when (action) {
            ColorAction.Initialize -> {
                updateState(cachedState.copy(initialize = SyncState.SYNC))
                fetchColor()
            }
            is ColorAction.Filter -> {
                updateState(cachedState.copy(filter = SyncState.SYNC))
                filterColor(action.query)
            }
        }
    }

    private fun fetchColor() = launch {
        val result = remoteDataSource.tryFetch()
        when (result) {
            is ColorFetchResult.Success -> {
                mutableResult.send(result)
                cachedItem = result
                updateState(cachedState.copy(initialize = SyncState.COMPLETED))
            }
            ColorFetchResult.Failure ->
                updateState(cachedState.copy(initialize = SyncState.FAILED))
        }
    }

    private fun filterColor(query: String) = launch {
        val item = cachedItem!!
        mutableResult.send(item.copy(
                colors = item.colors.filter { it.name.startsWith(query) }
        ))
        updateState(cachedState.copy(filter = SyncState.COMPLETED))
    }

    private fun updateState(state: ColorState) {
        cachedState = state
        mutableState.offer(cachedState)
    }

    enum class SyncState {
        SYNC,
        COMPLETED,
        FAILED
    }
}

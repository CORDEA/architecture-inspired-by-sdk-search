package jp.cordea.sdksearcharchitecturedemo

import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class ColorSynchronizer @Inject constructor(
        private val remoteDataSource: ColorRemoteDataSource
) {
    val state: ReceiveChannel<SyncState> get() = mutableState.openSubscription()
    val result: ReceiveChannel<ColorResult> get() = mutableResult.openSubscription()

    private var cachedItem: ColorFetchResult.Success? = null
    private val mutableState = ConflatedBroadcastChannel<SyncState>()
    private val mutableResult = ConflatedBroadcastChannel<ColorResult>()

    fun sync(action: ColorAction) {
        when (action) {
            ColorAction.Initialize -> {
                mutableState.offer(SyncState.SYNC)
                fetchColor()
            }
            is ColorAction.Filter -> filterColor(action.query)
        }
    }

    private fun fetchColor() = launch {
        val result = remoteDataSource.tryFetch()
        when (result) {
            is ColorFetchResult.Success -> {
                mutableResult.send(result)
                cachedItem = result
                mutableState.send(SyncState.COMPLETED)
            }
            ColorFetchResult.Failure ->
                mutableState.send(SyncState.FAILED)
        }
    }

    private fun filterColor(query: String) = launch {
        cachedItem?.let {
            mutableResult.send(it.copy(
                    colors = it.colors.filter { it.name.startsWith(query) }
            ))
        }
    }

    enum class SyncState {
        SYNC,
        COMPLETED,
        FAILED
    }
}

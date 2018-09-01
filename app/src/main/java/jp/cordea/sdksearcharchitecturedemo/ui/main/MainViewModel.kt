package jp.cordea.sdksearcharchitecturedemo.ui.main

import androidx.lifecycle.ViewModel
import jp.cordea.sdksearcharchitecturedemo.BaseViewModel
import jp.cordea.sdksearcharchitecturedemo.ColorAction
import jp.cordea.sdksearcharchitecturedemo.ColorFetchResult
import jp.cordea.sdksearcharchitecturedemo.ColorSynchronizer
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class MainViewModel : ViewModel(),
        BaseViewModel<MainViewModel.Model>,
        EventDispatcher {

    @Inject
    lateinit var synchronizer: ColorSynchronizer

    private val mutableModels: ConflatedBroadcastChannel<Model> = ConflatedBroadcastChannel()
    override val models: ReceiveChannel<Model> get() = mutableModels.openSubscription()

    private val mutableEvents: RendezvousChannel<Event> = RendezvousChannel()
    override val events: SendChannel<Event> get() = mutableEvents

    private val job = Job()
    private var model: MainViewModel.Model = Model()

    override fun start() {
        launch(UI, parent = job) {
            synchronizer.state.consumeEach {
                updateModel(model.copy(
                        initializeState = it.initialize.toSyncState(),
                        filterState = it.filter.toSyncState()
                ))
            }
        }

        launch(UI, parent = job) {
            synchronizer.result.consumeEach { result ->
                updateModel(when (result) {
                    is ColorFetchResult -> model.copy(
                            items = (result as ColorFetchResult.Success).colors
                                    .map { MainListItemModel(it) }
                    )
                })
            }
        }

        launch(UI, parent = job) {
            mutableEvents.consumeEach {
                when (it) {
                    is MainViewModel.Event.QueryChanged ->
                        synchronizer.sync(ColorAction.Filter(it.query))
                }
            }
        }

        synchronizer.sync(ColorAction.Initialize)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private fun updateModel(newModel: MainViewModel.Model) {
        model = newModel
        mutableModels.offer(newModel)
    }

    private fun ColorSynchronizer.SyncState.toSyncState(): SyncState =
            when (this) {
                ColorSynchronizer.SyncState.SYNC -> SyncState.SYNC
                ColorSynchronizer.SyncState.COMPLETED -> SyncState.COMPLETED
                ColorSynchronizer.SyncState.FAILED -> SyncState.FAILED
            }

    sealed class Event {
        data class QueryChanged(val query: String) : Event()
    }

    data class Model(
            val initializeState: SyncState = SyncState.SYNC,
            val filterState: SyncState = SyncState.SYNC,
            val items: List<MainListItemModel> = emptyList()
    )

    enum class SyncState {
        SYNC,
        COMPLETED,
        FAILED;

        val isCompleted get() = this == COMPLETED
        val isFailed get() = this == FAILED
    }
}

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
                when (it) {
                    ColorSynchronizer.SyncState.SYNC ->
                        updateModel(model.copy(state = SyncState.SYNC))
                    ColorSynchronizer.SyncState.FAILED ->
                        updateModel(model.copy(state = SyncState.FAILED))
                    ColorSynchronizer.SyncState.COMPLETED ->
                        updateModel(model.copy(state = SyncState.COMPLETED))
                }
            }
        }

        launch(UI, parent = job) {
            synchronizer.result.consumeEach {
                updateModel(when (it) {
                    is ColorFetchResult -> model.copy(
                            items = (it as ColorFetchResult.Success).colors
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

    sealed class Event {
        data class QueryChanged(val query: String) : Event()
    }

    data class Model(
            val state: SyncState = SyncState.SYNC,
            val items: List<MainListItemModel> = emptyList()
    )

    enum class SyncState {
        SYNC,
        COMPLETED,
        FAILED
    }
}

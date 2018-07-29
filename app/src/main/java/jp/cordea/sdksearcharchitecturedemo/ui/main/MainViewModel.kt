package jp.cordea.sdksearcharchitecturedemo.ui.main

import androidx.lifecycle.ViewModel
import jp.cordea.sdksearcharchitecturedemo.BaseViewModel
import jp.cordea.sdksearcharchitecturedemo.ColorStore
import jp.cordea.sdksearcharchitecturedemo.ColorSynchronizer
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class MainViewModel : ViewModel(), BaseViewModel<MainViewModel.Model, MainViewModel.Event> {

    @Inject
    lateinit var synchronizer: ColorSynchronizer
    @Inject
    lateinit var store: ColorStore

    private val mutableModels: ConflatedBroadcastChannel<Model> = ConflatedBroadcastChannel()
    override val models: ReceiveChannel<Model> get() = mutableModels.openSubscription()

    private val mutableEvents: RendezvousChannel<Event> = RendezvousChannel()
    override val events: SendChannel<Event> = mutableEvents

    private var model: MainViewModel.Model = Model()

    override fun start(): Job {
        val job = Job()
        model = Model()

        launch(UI, parent = job) {
            synchronizer.state.consumeEach {
                when (it) {
                    ColorSynchronizer.SyncState.SYNC ->
                        updateModel(model.copy(state = SyncState.SYNC))
                    ColorSynchronizer.SyncState.FAILED ->
                        updateModel(model.copy(state = SyncState.FAILED))
                    ColorSynchronizer.SyncState.COMPLETED -> store.fetch()
                }
            }
        }

        launch(UI, parent = job) {
            store.items.consumeEach {
                updateModel(model.copy(
                        state = SyncState.COMPLETED,
                        items = it.map { MainListItemModel(it) }
                ))
            }
        }

        launch(UI, parent = job) {
            mutableEvents.consumeEach {
                when (it) {
                    is MainViewModel.Event.QueryChanged -> store.fetchBy(it.query)
                }
            }
        }

        synchronizer.sync()

        return job
    }

    private fun updateModel(newModel: MainViewModel.Model) {
        model = newModel
        mutableModels.offer(newModel)
    }

    sealed class Event {
        data class QueryChanged(val query: String) : Event()
    }

    data class Model(
            val state: SyncState = SyncState.COMPLETED,
            val items: List<MainListItemModel> = emptyList()
    )

    enum class SyncState {
        SYNC,
        COMPLETED,
        FAILED
    }
}

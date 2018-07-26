package jp.cordea.sdksearcharchitecturedemo.ui.main

import android.arch.lifecycle.ViewModel
import jp.cordea.sdksearcharchitecturedemo.BaseViewModel
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.RendezvousChannel
import kotlinx.coroutines.experimental.channels.SendChannel

class MainViewModel : ViewModel(), BaseViewModel<MainViewModel.Model, MainViewModel.Event> {

    private val mutableModels: ConflatedBroadcastChannel<Model> = ConflatedBroadcastChannel()
    override val models: ReceiveChannel<Model> = mutableModels.openSubscription()

    private val mutableEvents: RendezvousChannel<Event> = RendezvousChannel()
    override val events: SendChannel<Event> = mutableEvents

    override fun start(): Job {
    }

    sealed class Model
    sealed class Event
}

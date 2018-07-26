package jp.cordea.sdksearcharchitecturedemo

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.SendChannel

interface BaseViewModel<M, E> {
    val models: ReceiveChannel<M>
    val events: SendChannel<E>
    fun start(): Job
}

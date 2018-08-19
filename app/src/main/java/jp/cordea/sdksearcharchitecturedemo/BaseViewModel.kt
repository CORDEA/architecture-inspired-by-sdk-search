package jp.cordea.sdksearcharchitecturedemo

import kotlinx.coroutines.experimental.channels.ReceiveChannel

interface BaseViewModel<M> {
    val models: ReceiveChannel<M>
    fun start()
}

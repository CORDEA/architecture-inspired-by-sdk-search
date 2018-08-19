package jp.cordea.sdksearcharchitecturedemo.ui.main

import kotlinx.coroutines.experimental.channels.SendChannel

interface EventDispatcher {
    val events: SendChannel<MainViewModel.Event>
}

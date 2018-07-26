package jp.cordea.sdksearcharchitecturedemo

import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
class ColorSynchronizer {
    private val random: Random = Random()
    private val mutableState: ConflatedBroadcastChannel<SyncState> = ConflatedBroadcastChannel()
    val state: ReceiveChannel<SyncState> = mutableState.openSubscription()

    fun sync() {
        mutableState.offer(SyncState.SYNC)
        launch {
            mutableState.offer(if (tryFetch()) SyncState.COMPLETED else SyncState.FAILED)
        }
    }

    private suspend fun tryFetch(): Boolean {
        delay(random.nextInt(4).toLong(), TimeUnit.SECONDS)
        return random.nextBoolean()
    }

    enum class SyncState {
        SYNC,
        COMPLETED,
        FAILED
    }
}

package jp.cordea.sdksearcharchitecturedemo

import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.delay
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorStore @Inject constructor() {
    private var cachedItems: List<Color> = emptyList()
    private val mutableItems: ConflatedBroadcastChannel<List<Color>> =
            ConflatedBroadcastChannel()
    val items: ReceiveChannel<List<Color>> get() = mutableItems.openSubscription()

    suspend fun update(items: List<Color>) {
        delay(Random().nextInt(2).toLong(), TimeUnit.SECONDS)
        this.cachedItems = items
    }

    fun fetch() {
        mutableItems.offer(cachedItems)
    }

    fun fetchBy(query: String) {
        mutableItems.offer(cachedItems.filter { it.name.startsWith(query) })
    }
}

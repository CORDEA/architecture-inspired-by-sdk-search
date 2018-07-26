package jp.cordea.sdksearcharchitecturedemo

import kotlinx.coroutines.experimental.delay
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
class ColorStore {
    private var items: List<Color> = emptyList()

    suspend fun update(items: List<Color>) {
        delay(Random().nextInt(2).toLong(), TimeUnit.SECONDS)
        this.items = items
    }

    fun get(): List<Color> = items
}

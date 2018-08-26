package jp.cordea.sdksearcharchitecturedemo

import kotlinx.coroutines.experimental.delay
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorRemoteDataSource @Inject constructor() {
    private val random: Random = Random()

    suspend fun tryFetch(): ColorFetchResult {
        delay(random.nextInt(4).toLong(), TimeUnit.SECONDS)
        return ColorFetchResult.Success(emptyList())
    }
}

package jp.cordea.sdksearcharchitecturedemo

import com.google.common.truth.Truth
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class ColorStoreTest {

    private val store = ColorStore()

    @Test
    fun fetch() = runBlocking {
        val items = store.items

        store.fetch()
        Truth.assertThat(items.receive()).isEmpty()

        store.update(listOf(Color("name", "hex")))
        store.fetch()
        Truth.assertThat(items.receive()).hasSize(1)
    }

    @Test
    fun fetchBy() = runBlocking {
        val items = store.items

        store.fetchBy("")
        Truth.assertThat(items.receive()).isEmpty()

        store.update(listOf(Color("1", ""), Color("22", ""), Color("12", "")))
        store.fetchBy("1")
        Truth.assertThat(items.receive()).hasSize(2)

        store.fetchBy("2")
        Truth.assertThat(items.receive()).hasSize(1)
    }
}

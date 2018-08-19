package jp.cordea.sdksearcharchitecturedemo.ui.main

import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import jp.cordea.sdksearcharchitecturedemo.Color
import jp.cordea.sdksearcharchitecturedemo.ColorStore
import jp.cordea.sdksearcharchitecturedemo.ColorSynchronizer
import kotlinx.coroutines.experimental.channels.RendezvousChannel
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class MainViewModelTest {

    @Mock
    private lateinit var synchronizer: ColorSynchronizer
    @Mock
    private lateinit var store: ColorStore

    @Before
    fun setUp() = MockitoAnnotations.initMocks(this)

    @Test
    fun `start - state`() = runBlocking {
        val channel = RendezvousChannel<ColorSynchronizer.SyncState>()
        whenever(synchronizer.state).thenReturn(channel)
        whenever(store.items).thenReturn(RendezvousChannel())
        val viewModel = MainViewModel().also {
            it.synchronizer = synchronizer
            it.store = store
        }
        viewModel.start()

        channel.send(ColorSynchronizer.SyncState.SYNC)
        Truth.assertThat(viewModel.models.receive().state)
                .isEqualTo(MainViewModel.SyncState.SYNC)

        channel.send(ColorSynchronizer.SyncState.COMPLETED)
        verify(store).fetch()
    }

    @Test
    fun `start - items`() = runBlocking {
        val channel = RendezvousChannel<List<Color>>()
        whenever(synchronizer.state).thenReturn(RendezvousChannel())
        whenever(store.items).thenReturn(channel)
        val viewModel = MainViewModel().also {
            it.synchronizer = synchronizer
            it.store = store
        }
        viewModel.start()

        channel.send(listOf(mock(), mock()))
        val model = viewModel.models.receive()
        Truth.assertThat(model.items.size).isEqualTo(2)
        Truth.assertThat(model.state).isEqualTo(MainViewModel.SyncState.COMPLETED)
    }

    @Test
    fun `start - events`() = runBlocking {
        whenever(synchronizer.state).thenReturn(RendezvousChannel())
        whenever(store.items).thenReturn(RendezvousChannel())
        val viewModel = MainViewModel().also {
            it.synchronizer = synchronizer
            it.store = store
        }
        viewModel.start()

        viewModel.events.offer(MainViewModel.Event.QueryChanged("a"))
        verify(store).fetchBy("a")
    }
}

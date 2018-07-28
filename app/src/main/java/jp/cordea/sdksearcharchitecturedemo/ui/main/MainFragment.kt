package jp.cordea.sdksearcharchitecturedemo.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.AndroidSupportInjection
import jp.cordea.sdksearcharchitecturedemo.ColorSynchronizer
import jp.cordea.sdksearcharchitecturedemo.databinding.MainFragmentBinding
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject
import javax.inject.Provider

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModel: MainViewModel
    @Inject
    lateinit var listItem: Provider<MainListItem>

    private val adapter: GroupAdapter<ViewHolder> = GroupAdapter()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = MainFragmentBinding.inflate(inflater, container, false).also {
        it.recyclerView.adapter = adapter
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribe()
        viewModel.start()
    }

    private fun subscribe() {
        launch(UI) {
            viewModel.models.consumeEach {
                if (it.state == ColorSynchronizer.SyncState.COMPLETED) {
                    adapter.addAll(it.items.map { listItem.get().apply { model = it } })
                }
            }
        }
    }
}

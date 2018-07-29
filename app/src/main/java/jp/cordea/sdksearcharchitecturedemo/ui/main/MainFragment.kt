package jp.cordea.sdksearcharchitecturedemo.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.AndroidSupportInjection
import jp.cordea.sdksearcharchitecturedemo.R
import jp.cordea.sdksearcharchitecturedemo.databinding.MainFragmentBinding
import kotlinx.coroutines.experimental.Job
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
    private lateinit var childViewJob: Job

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = MainFragmentBinding.inflate(inflater, container, false).also {
        setHasOptionsMenu(true)
        it.recyclerView.adapter = adapter
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribe()
        viewModel.start()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        val searchView = (menu.findItem(R.id.search).actionView as SearchView)
        childViewJob = SearchBinder(searchView).bind(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        childViewJob.cancel()
    }

    private fun subscribe() {
        launch(UI) {
            viewModel.models.consumeEach {
                if (it.state == MainViewModel.SyncState.COMPLETED) {
                    adapter.addAll(it.items.map { listItem.get().apply { model = it } })
                }
            }
        }
    }
}

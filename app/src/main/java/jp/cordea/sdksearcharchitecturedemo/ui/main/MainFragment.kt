package jp.cordea.sdksearcharchitecturedemo.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import jp.cordea.sdksearcharchitecturedemo.R
import jp.cordea.sdksearcharchitecturedemo.databinding.MainFragmentBinding
import kotlinx.coroutines.experimental.Job
import javax.inject.Inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModel: MainViewModel
    @Inject
    lateinit var adapter: MainAdapter

    private lateinit var binding: MainFragmentBinding
    private lateinit var job: Job
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
        binding = it
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        job = MainUiBinder(binding, adapter).bindTo(viewModel)
        if (savedInstanceState == null) {
            viewModel.start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        val searchView = (menu.findItem(R.id.search).actionView as SearchView)
        childViewJob = SearchUiBinder(searchView).bindTo(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        childViewJob.cancel()
        job.cancel()
    }
}

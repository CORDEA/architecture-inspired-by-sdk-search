package jp.cordea.sdksearcharchitecturedemo.ui.main

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.RendezvousChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

class SearchBinder(view: SearchView) {
    private val mutableQueryTextChanged: RendezvousChannel<String> = RendezvousChannel()
    private val queryTextChanged: ReceiveChannel<String> = mutableQueryTextChanged

    init {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                mutableQueryTextChanged.offer(newText)
                return true
            }
        })
    }

    fun bind(viewModel: MainViewModel): Job {
        val job = Job()
        launch(parent = job) {
            queryTextChanged.consumeEach {
                viewModel.events.offer(MainViewModel.Event.QueryChanged(it))
            }
        }
        return job
    }
}

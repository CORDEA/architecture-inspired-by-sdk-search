package jp.cordea.sdksearcharchitecturedemo.ui.main

import androidx.appcompat.widget.SearchView

class SearchUiBinder(
        view: SearchView
) : UiBinder<MainViewModel.Model> {
    init {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
//            viewModel.events.offer(MainViewModel.Event.QueryChanged(it))
                return true
            }
        })
    }

    override fun bind(model: MainViewModel.Model, oldModel: MainViewModel.Model?) {
    }
}

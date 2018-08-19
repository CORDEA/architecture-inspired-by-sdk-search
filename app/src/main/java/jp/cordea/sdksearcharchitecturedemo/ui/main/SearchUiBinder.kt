package jp.cordea.sdksearcharchitecturedemo.ui.main

import androidx.appcompat.widget.SearchView

class SearchUiBinder(
        private val view: SearchView,
        private val eventDispatcher: EventDispatcher
) : UiBinder<MainViewModel.Model> {

    override fun bind(model: MainViewModel.Model, oldModel: MainViewModel.Model?) {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                eventDispatcher.events.offer(MainViewModel.Event.QueryChanged(newText))
                return true
            }
        })
    }
}

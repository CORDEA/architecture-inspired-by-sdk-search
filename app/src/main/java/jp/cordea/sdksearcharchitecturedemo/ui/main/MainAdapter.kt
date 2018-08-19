package jp.cordea.sdksearcharchitecturedemo.ui.main

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import javax.inject.Inject
import javax.inject.Provider

class MainAdapter @Inject constructor(
        private val listItem: Provider<MainListItem>
) : GroupAdapter<ViewHolder>() {

    fun init(items: List<MainListItemModel>) {
        addAll(items.map { listItem.get().apply { model = it } })
    }
}

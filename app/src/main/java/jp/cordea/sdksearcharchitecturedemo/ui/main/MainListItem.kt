package jp.cordea.sdksearcharchitecturedemo.ui.main

import com.xwray.groupie.databinding.BindableItem
import jp.cordea.sdksearcharchitecturedemo.Color
import jp.cordea.sdksearcharchitecturedemo.R
import jp.cordea.sdksearcharchitecturedemo.databinding.MainListItemBinding
import javax.inject.Inject

class MainListItemModel(
        data: Color
) {
    val title: String = data.name
    val description: String = data.hex
}

class MainListItem @Inject constructor(
) : BindableItem<MainListItemBinding>() {
    lateinit var model: MainListItemModel

    override fun getLayout(): Int = R.layout.main_list_item

    override fun bind(binding: MainListItemBinding, position: Int) {
        binding.model = model
    }
}

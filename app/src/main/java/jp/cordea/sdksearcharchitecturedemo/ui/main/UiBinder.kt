package jp.cordea.sdksearcharchitecturedemo.ui.main

import jp.cordea.sdksearcharchitecturedemo.BaseViewModel
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.launch

interface UiBinder<T : Any> {
    fun bind(model: T, oldModel: T?)
}

fun <T : Any> UiBinder<T>.bindTo(viewModel: BaseViewModel<T>) =
        launch(Unconfined) {
            var oldModel: T? = null
            for (model in viewModel.models) {
                bind(model, oldModel)
                oldModel = model
            }
        }

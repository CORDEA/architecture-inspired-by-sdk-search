package jp.cordea.sdksearcharchitecturedemo.ui.main

import androidx.core.view.isVisible
import jp.cordea.sdksearcharchitecturedemo.databinding.MainFragmentBinding

class MainUiBinder(
        private val binding: MainFragmentBinding,
        private val adapter: MainAdapter
) : UiBinder<MainViewModel.Model> {

    override fun bind(model: MainViewModel.Model, oldModel: MainViewModel.Model?) {
        if (model == oldModel) {
            return
        }
        adapter.clear()
        when (model.state) {
            MainViewModel.SyncState.SYNC -> {
                binding.errorView.isVisible = false
                binding.progressView.isVisible = true
            }
            MainViewModel.SyncState.COMPLETED -> {
                binding.errorView.isVisible = false
                binding.progressView.isVisible = false
                adapter.init(model.items)
            }
            MainViewModel.SyncState.FAILED -> {
                binding.progressView.isVisible = false
                binding.errorView.isVisible = true
            }
        }
    }
}

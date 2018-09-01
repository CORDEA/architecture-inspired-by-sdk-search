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
        switchVisibleState(model, oldModel)
        if (model.filterState.isCompleted) {
            adapter.clear()
            adapter.init(model.items)
            return
        }
        if (model.initializeState.isCompleted) {
            if (adapter.itemCount > 0) {
                return
            }
            adapter.init(model.items)
        }
    }

    private fun switchVisibleState(model: MainViewModel.Model, oldModel: MainViewModel.Model?) {
        if (model.initializeState == oldModel?.initializeState) {
            return
        }
        when (model.initializeState) {
            MainViewModel.SyncState.SYNC -> {
                binding.errorView.isVisible = false
                binding.progressView.isVisible = true
            }
            MainViewModel.SyncState.COMPLETED -> {
                binding.errorView.isVisible = false
                binding.progressView.isVisible = false
            }
            MainViewModel.SyncState.FAILED -> {
                binding.progressView.isVisible = false
                binding.errorView.isVisible = true
            }
        }
    }
}

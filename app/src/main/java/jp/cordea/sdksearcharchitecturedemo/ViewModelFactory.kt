package jp.cordea.sdksearcharchitecturedemo

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.MembersInjector
import javax.inject.Inject

class ViewModelFactory<V : ViewModel> @Inject constructor(
        private val injector: MembersInjector<V>
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            super.create(modelClass).apply { injector.injectMembers(this as V) }
}

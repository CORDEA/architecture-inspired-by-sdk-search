package jp.cordea.sdksearcharchitecturedemo

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModelStoreOwner
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import dagger.Module
import dagger.Provides
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule<T : ViewModel>(
        private val kClass: KClass<T>
) {
    @Provides
    fun provideViewModel(
            owner: ViewModelStoreOwner,
            factory: ViewModelFactory<T>
    ): T {
        if (owner is Fragment) {
            return ViewModelProviders.of(owner, factory).get(kClass.java)
        }
        if (owner is FragmentActivity) {
            return ViewModelProviders.of(owner, factory).get(kClass.java)
        }
        throw IllegalArgumentException()
    }
}

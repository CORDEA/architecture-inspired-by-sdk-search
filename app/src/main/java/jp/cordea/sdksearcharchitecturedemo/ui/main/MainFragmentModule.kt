package jp.cordea.sdksearcharchitecturedemo.ui.main

import androidx.lifecycle.ViewModelStoreOwner
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.cordea.sdksearcharchitecturedemo.ViewModelModule

@Module
interface MainFragmentModule {
    @ContributesAndroidInjector(modules = [
        MainFragmentBindModule::class,
        MainViewModelModule::class
    ])
    fun contributeMainFragment(): MainFragment
}

@Module
interface MainFragmentBindModule {
    @Binds
    fun bindViewModelStoreOwner(fragment: MainFragment): ViewModelStoreOwner
}

@Module
class MainViewModelModule : ViewModelModule<MainViewModel>(MainViewModel::class)

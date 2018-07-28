package jp.cordea.sdksearcharchitecturedemo

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.cordea.sdksearcharchitecturedemo.ui.main.MainFragmentModule

@Module
interface MainActivityModule {
    @ContributesAndroidInjector(modules = [
        MainFragmentModule::class
    ])
    fun contributeMainActivity(): MainActivity
}

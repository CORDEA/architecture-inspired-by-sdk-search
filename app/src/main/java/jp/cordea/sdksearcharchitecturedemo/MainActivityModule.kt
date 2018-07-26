package jp.cordea.sdksearcharchitecturedemo

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityModule {
    @ContributesAndroidInjector(modules = [

    ])
    fun contributeMainActivity(): MainActivity
}

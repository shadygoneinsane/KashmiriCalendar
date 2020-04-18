package koushur.kashmirievents.di.module.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import koushur.kashmirievents.presentation.ui.main.ActivityMain

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): ActivityMain
}
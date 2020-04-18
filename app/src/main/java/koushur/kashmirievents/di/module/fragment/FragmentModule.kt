package koushur.kashmirievents.di.module.fragment

import koushur.kashmirievents.presentation.ui.main.LandingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeLandingFragment(): LandingFragment
}
package koushur.kashmirievents.di.module.application

import koushur.kashmirievents.presentation.ui.main.calendar.LandingViewModel
import koushur.kashmirievents.presentation.ui.main.featured.FeaturedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LandingViewModel() }
    viewModel { FeaturedViewModel(context = androidContext(), remoteConfigManager = get()) }
}
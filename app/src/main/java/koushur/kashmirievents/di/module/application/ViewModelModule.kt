package koushur.kashmirievents.di.module.application

import kotlinx.coroutines.Dispatchers
import koushur.kashmirievents.di.module.DefaultDispatcherProvider
import koushur.kashmirievents.presentation.ui.main.calendar.LandingViewModel
import koushur.kashmirievents.presentation.ui.main.featured.FeaturedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LandingViewModel(DefaultDispatcherProvider()) }
    viewModel { FeaturedViewModel(context = androidContext(), remoteConfigManager = get()) }
}
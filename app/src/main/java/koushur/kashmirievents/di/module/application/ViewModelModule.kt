package koushur.kashmirievents.di.module.application

import koushur.kashmirievents.di.module.DefaultDispatcherProvider
import koushur.kashmirievents.presentation.ui.main.addevent.AddEventViewModel
import koushur.kashmirievents.presentation.ui.main.calendar.LandingViewModel
import koushur.kashmirievents.presentation.ui.main.featured.FeaturedViewModel
import koushur.kashmirievents.presentation.ui.main.savedevents.SavedEventsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LandingViewModel(repository = get(), DefaultDispatcherProvider()) }
    viewModel { FeaturedViewModel(context = androidContext(), remoteConfigManager = get()) }
    viewModel { AddEventViewModel(repository = get(), dispatcher = DefaultDispatcherProvider()) }
    viewModel { SavedEventsViewModel(repository = get(), dispatcher = DefaultDispatcherProvider()) }
}
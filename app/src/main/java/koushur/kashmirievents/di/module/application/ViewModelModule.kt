package koushur.kashmirievents.di.module.application

import koushur.kashmirievents.presentation.ui.main.LandingViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { LandingViewModel(repository = get()) }
}
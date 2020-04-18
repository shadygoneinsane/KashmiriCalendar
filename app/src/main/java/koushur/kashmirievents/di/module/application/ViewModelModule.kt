package koushur.kashmirievents.di.module.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import koushur.kashmirievents.presentation.ui.main.LandingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import koushur.app.di.module.application.ViewModelFactory
import koushur.kashmirievents.di.qualifier.ViewModelKey

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LandingViewModel::class)
    internal abstract fun bindLandingFragmentViewModel(viewModel: LandingViewModel): ViewModel
}
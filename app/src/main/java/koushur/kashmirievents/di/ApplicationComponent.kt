package koushur.kashmirievents.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import koushur.kashmirievents.BaseApplication
import koushur.kashmirievents.di.module.activity.ActivityModule
import koushur.kashmirievents.di.module.application.DbModule
import koushur.kashmirievents.di.module.application.RepositoryModule
import koushur.kashmirievents.di.module.application.ViewModelModule
import koushur.kashmirievents.di.module.fragment.FragmentModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        RepositoryModule::class,
        FragmentModule::class,
        DbModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<BaseApplication>()
}

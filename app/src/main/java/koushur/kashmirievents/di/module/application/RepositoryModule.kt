package koushur.kashmirievents.di.module.application

import dagger.Module
import dagger.Provides
import koushur.kashmirievents.repository.WeatherRepository

@Module
class RepositoryModule {
    @Provides
    fun provideSampleRepository(): WeatherRepository {
        return WeatherRepository()
    }
}
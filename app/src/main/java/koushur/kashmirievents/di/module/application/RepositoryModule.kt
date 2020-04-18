package koushur.kashmirievents.di.module.application

import dagger.Module
import dagger.Provides
import koushur.kashmirievents.database.dao.MonthDataDao
import koushur.kashmirievents.repository.CalendarRepository

@Module(includes = [DbModule::class])
class RepositoryModule {
    @Provides
    fun provideSampleRepository(monthDataDao: MonthDataDao): CalendarRepository {
        return CalendarRepository(monthDataDao)
    }
}
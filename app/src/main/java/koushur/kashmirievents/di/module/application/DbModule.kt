package koushur.kashmirievents.di.module.application

import androidx.room.Room
import dagger.Module
import dagger.Provides
import koushur.kashmirievents.BaseApplication
import koushur.kashmirievents.database.AppDb
import koushur.kashmirievents.database.dao.MonthDataDao
import javax.inject.Singleton

/**
 * Created by Vikesh Dass
 */
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDb {
        return Room
            .databaseBuilder(BaseApplication.applicationContext(), AppDb::class.java, "calendar.db")
            .createFromAsset("database/calendar.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMonthDataDao(appDb: AppDb): MonthDataDao {
        return appDb.monthDataDao()
    }
}
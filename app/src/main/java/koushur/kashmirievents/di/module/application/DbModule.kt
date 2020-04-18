package vikesh.dass.lockmeout.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import vikesh.dass.lockmeout.LockApplication
import vikesh.dass.lockmeout.local.dao.RunningLockProfileDao
import vikesh.dass.lockmeout.local.dao.ScheduleLockProfileDao
import vikesh.dass.lockmeout.local.database.AppDb
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
                .databaseBuilder(LockApplication.applicationContext(), AppDb::class.java, "KeepMeOut.db")
                .addMigrations(migration_1_2)
                .fallbackToDestructiveMigration()
                .build()
    }

    /**
     * Use this to migrate when we need to update Db schemas
     */
    private val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            //database.execSQL("CREATE TABLE `ScheduleLockProfile` (`profileId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `startTimeMillis` INTEGER NOT NULL, `startTimeInHumanReadableFormat` TEXT NOT NULL, `endTimeMillis` INTEGER NOT NULL, `endTimeInHumanReadableFormat` TEXT NOT NULL, `lockDurationMillis` INTEGER NOT NULL, `enabledStatus` INTEGER NOT NULL, `runningStatus` INTEGER NOT NULL, `sunday` INTEGER NOT NULL, `monday` INTEGER NOT NULL, `tuesday` INTEGER NOT NULL, `wednesday` INTEGER NOT NULL, `thursday` INTEGER NOT NULL, `friday` INTEGER NOT NULL, `saturday` INTEGER NOT NULL)")
            //database.execSQL("ALTER TABLE 'RunningLockProfile' ADD COLUMN 'lockDurationI' INTEGER NOT NULL DEFAULT 10")
        }
    }

    @Provides
    @Singleton
    fun provideScheduleLockProfileDao(appDb: AppDb): ScheduleLockProfileDao {
        return appDb.scheduleLockProfileDao()
    }

    @Provides
    @Singleton
    fun provideRunningLockProfileDao(appDb: AppDb): RunningLockProfileDao {
        return appDb.runningLockProfileDao()
    }
}
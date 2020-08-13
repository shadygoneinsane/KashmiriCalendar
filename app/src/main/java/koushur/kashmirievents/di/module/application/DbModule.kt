package koushur.kashmirievents.di.module.application

import androidx.room.Room
import koushur.kashmirievents.database.AppDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by Vikesh Dass
 */
val DbModule = module {
    single { Room.databaseBuilder(androidContext(), AppDb::class.java, "calendarData.db").build() }
    single { get<AppDb>().monthDataDao() }
}
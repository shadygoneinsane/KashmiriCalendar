package koushur.kashmirievents.di.module.application

import androidx.room.Room
import koushur.kashmirievents.database.AppDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by Vikesh Dass
 * Email : vikeshdass@gmail.com
 */
val dbModule = module {
    val dbName = "calendarData.db"
    single {
        Room
            .databaseBuilder(androidApplication(), AppDb::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDb>().eventsDataDao() }
}
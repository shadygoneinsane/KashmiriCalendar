package koushur.kashmirievents

import android.app.Application
import android.content.Context
import koushir.kashmirievents.BuildConfig
import koushur.kashmirievents.di.module.application.DbModule
import koushur.kashmirievents.di.module.application.remoteRepositoryModule
import koushur.kashmirievents.di.module.application.retrofitModule
import koushur.kashmirievents.di.module.application.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class BaseApplication : Application(), KoinComponent {
    companion object {
        private lateinit var instance: BaseApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(DbModule, remoteRepositoryModule, retrofitModule, viewModelModule)
        }
    }
}
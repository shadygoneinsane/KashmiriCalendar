package koushur.kashmirievents

import android.app.Application
import android.content.Context
import koushir.kashmirievents.BuildConfig
import koushur.kashmirievents.di.module.application.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class KashmiriEventsApplication : Application() {
    companion object {
        private lateinit var instance: KashmiriEventsApplication
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
            androidContext(this@KashmiriEventsApplication)
            modules(mainModule, dbModule, remoteRepositoryModule, retrofitModule, viewModelModule)
        }
    }
}
package koushur.kashmirievents

import android.app.Application
import android.content.Context
import android.content.res.Resources
import koushir.kashmirievents.BuildConfig
import koushur.kashmirievents.di.module.application.dbModule
import koushur.kashmirievents.di.module.application.mainModule
import koushur.kashmirievents.di.module.application.remoteRepositoryModule
import koushur.kashmirievents.di.module.application.retrofitModule
import koushur.kashmirievents.di.module.application.viewModelModule
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

        fun fetchResources(): Resources {
            return instance.applicationContext.resources
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
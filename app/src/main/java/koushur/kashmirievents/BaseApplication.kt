package koushur.kashmirievents

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import koushir.kashmirievents.BuildConfig
import koushur.kashmirievents.di.DaggerApplicationComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }

    companion object {
        private lateinit var instance: BaseApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
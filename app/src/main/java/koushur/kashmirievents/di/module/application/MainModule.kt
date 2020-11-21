package koushur.kashmirievents.di.module.application

import koushur.kashmirievents.network.remote.RemoteConfigManager
import org.koin.dsl.module

/**
 * DI Main Module
 *
 * Author: Vikesh Dass
 * Created on: 21-11-2020
 * Email : vikeshdass@gmail.com
 */
val mainModule = module {
    single { RemoteConfigManager() }
}

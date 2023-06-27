package koushur.kashmirievents.di.module.application

import koushur.kashmirievents.repository.CalendarRepository
import org.koin.dsl.module

/**
 * This is repository module
 */
val remoteRepositoryModule = module {
    factory<CalendarRepository> { CalendarRepository(monthDataDao = get()) }
}
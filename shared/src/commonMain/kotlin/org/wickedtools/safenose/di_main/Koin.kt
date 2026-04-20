package org.wickedtools.safenose.di_main

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.wickedtools.safenose.core.di.coreModule

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin { appDeclaration()
        modules(coreModule)
    }
}
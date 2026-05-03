package org.wickedtools.safenose.di_main

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.wickedtools.safenose.core.di.coreModule
import org.wickedtools.safenose.feature_authentication.di.authModule
import org.wickedtools.safenose.feature_fragrance.di.fragranceModule
import org.wickedtools.safenose.feature_profile.di.profileModule
import org.wickedtools.safenose.feature_reviews.di.reviewsModule

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            coreModule,
            authModule,
            profileModule,
            fragranceModule,
            reviewsModule
        )
    }
}
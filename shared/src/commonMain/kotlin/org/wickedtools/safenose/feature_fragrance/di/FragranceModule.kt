package org.wickedtools.safenose.feature_fragrance.di

import org.koin.dsl.module
import org.wickedtools.safenose.feature_fragrance.data.repository.FragranceRepositoryImpl
import org.wickedtools.safenose.feature_fragrance.domain.repository.FragranceRepository
import org.wickedtools.safenose.feature_fragrance.domain.usecases.GetTrendingFragrancesUseCase
import org.wickedtools.safenose.feature_fragrance.domain.usecases.SearchFragrancesUseCase

val fragranceModule = module {
    single<FragranceRepository> { FragranceRepositoryImpl(get()) }
    factory { SearchFragrancesUseCase(get()) }
    factory { GetTrendingFragrancesUseCase(get()) }
}


package org.wickedtools.safenose.feature_profile.di

import org.koin.dsl.module
import org.wickedtools.safenose.feature_profile.data.repository.ProfileRepositoryImpl
import org.wickedtools.safenose.feature_profile.domain.repository.ProfileRepository
import org.wickedtools.safenose.feature_profile.domain.usecases.GetProfileUseCase
import org.wickedtools.safenose.feature_profile.domain.usecases.UpdateProfileUseCase
import org.wickedtools.safenose.feature_profile.presentation.viewmodels.ProfileViewModel
import org.wickedtools.safenose.feature_reviews.domain.usecases.GetUserReviewsUseCase

val profileModule = module {
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    factory { GetProfileUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { GetUserReviewsUseCase(get()) }
    factory { ProfileViewModel(get(), get(), get()) }
}
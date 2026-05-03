package org.wickedtools.safenose.feature_reviews.di

import org.koin.dsl.module
import org.wickedtools.safenose.feature_reviews.data.repository.ReviewRepositoryImpl
import org.wickedtools.safenose.feature_reviews.domain.repository.ReviewRepository
import org.wickedtools.safenose.feature_reviews.domain.usecases.GetUserReviewsUseCase
import org.wickedtools.safenose.feature_reviews.domain.usecases.SubmitReviewUseCase
import org.wickedtools.safenose.feature_reviews.presentation.viewmodels.AddReviewViewModel

val reviewsModule = module {
    single<ReviewRepository> { ReviewRepositoryImpl(get()) }
    factory { SubmitReviewUseCase(get()) }
    factory { GetUserReviewsUseCase(get()) }
    factory { AddReviewViewModel(get(), get(), get(), get()) }
}


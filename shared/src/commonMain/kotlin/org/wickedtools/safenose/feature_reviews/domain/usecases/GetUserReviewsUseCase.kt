package org.wickedtools.safenose.feature_reviews.domain.usecases

import org.wickedtools.safenose.feature_reviews.domain.model.UserReview
import org.wickedtools.safenose.feature_reviews.domain.repository.ReviewRepository

class GetUserReviewsUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(): List<UserReview> = repository.getUserReviews()
}

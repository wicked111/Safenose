package org.wickedtools.safenose.feature_reviews.domain.usecases

import org.wickedtools.safenose.feature_reviews.domain.repository.ReviewRepository

class SubmitReviewUseCase(private val repository: ReviewRepository) {
    suspend operator fun invoke(
        fragranceId: Long,
        overallRating: Int,
        longevityRank: String?,
        projectionRank: String?,
        valueRank: String?,
        seasonWorn: String?,
        reviewText: String?
    ) = repository.submitReview(
        fragranceId, overallRating, longevityRank, projectionRank, valueRank, seasonWorn, reviewText
    )
}

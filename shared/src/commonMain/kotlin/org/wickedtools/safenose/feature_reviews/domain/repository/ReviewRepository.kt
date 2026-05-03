package org.wickedtools.safenose.feature_reviews.domain.repository

import org.wickedtools.safenose.feature_reviews.domain.model.UserReview

interface ReviewRepository {
    suspend fun submitReview(
        fragranceId: Long,
        overallRating: Int,
        longevityRank: String?,
        projectionRank: String?,
        valueRank: String?,
        seasonWorn: String?,
        reviewText: String?
    )
    suspend fun getUserReviews(): List<UserReview>
}

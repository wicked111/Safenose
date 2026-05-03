package org.wickedtools.safenose.feature_reviews.domain.model

data class UserReview(
    val id: Long,
    val fragranceName: String,
    val brandName: String,
    val rating: Int,
    val longevityRank: String?,
    val projectionRank: String?,
    val valueRank: String?,
    val seasonWorn: String?,
    val reviewText: String?,
    val createdAt: String
)

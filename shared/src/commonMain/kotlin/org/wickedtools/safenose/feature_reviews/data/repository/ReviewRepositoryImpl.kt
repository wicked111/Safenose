package org.wickedtools.safenose.feature_reviews.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.Serializable
import org.wickedtools.safenose.feature_reviews.domain.model.UserReview
import org.wickedtools.safenose.feature_reviews.domain.repository.ReviewRepository

class ReviewRepositoryImpl(private val supabase: SupabaseClient) : ReviewRepository {

    override suspend fun submitReview(
        fragranceId: Long,
        overallRating: Int,
        longevityRank: String?,
        projectionRank: String?,
        valueRank: String?,
        seasonWorn: String?,
        reviewText: String?
    ) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: error("Not logged in")
        supabase.postgrest["reviews"].insert(
            ReviewInsert(
                user_id = userId,
                fragrance_id = fragranceId,
                rating = overallRating.toShort(),
                longevity_rank = longevityRank,
                projection_rank = projectionRank,
                value_rank = valueRank,
                season_worn = seasonWorn,
                review_text = reviewText
            )
        )
    }

    override suspend fun getUserReviews(): List<UserReview> {
        val userId = supabase.auth.currentUserOrNull()?.id ?: error("Not logged in")
        return supabase.postgrest["reviews"]
            .select(Columns.raw("id,created_at,rating,longevity_rank,projection_rank,value_rank,season_worn,review_text,fragrances(name,brands(name))")) {
                filter { eq("user_id", userId) }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<ReviewRow>()
            .map {
                UserReview(
                    id = it.id,
                    fragranceName = it.fragrances.name,
                    brandName = it.fragrances.brands.name,
                    rating = it.rating.toInt(),
                    longevityRank = it.longevity_rank,
                    projectionRank = it.projection_rank,
                    valueRank = it.value_rank,
                    seasonWorn = it.season_worn,
                    reviewText = it.review_text,
                    createdAt = it.created_at.take(10)
                )
            }
    }
}

@Serializable
private data class ReviewRow(
    val id: Long,
    val created_at: String,
    val rating: Short,
    val longevity_rank: String? = null,
    val projection_rank: String? = null,
    val value_rank: String? = null,
    val season_worn: String? = null,
    val review_text: String? = null,
    val fragrances: FragranceRef
)

@Serializable
private data class FragranceRef(val name: String, val brands: BrandRef)

@Serializable
private data class BrandRef(val name: String)

@Serializable
private data class ReviewInsert(
    val user_id: String,
    val fragrance_id: Long,
    val rating: Short,
    val longevity_rank: String?,
    val projection_rank: String?,
    val value_rank: String?,
    val season_worn: String?,
    val review_text: String?
)

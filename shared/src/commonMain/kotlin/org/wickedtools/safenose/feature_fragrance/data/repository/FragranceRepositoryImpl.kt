package org.wickedtools.safenose.feature_fragrance.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.Serializable
import org.wickedtools.safenose.feature_fragrance.domain.model.FragranceSummary
import org.wickedtools.safenose.feature_fragrance.domain.repository.FragranceRepository

class FragranceRepositoryImpl(private val supabase: SupabaseClient) : FragranceRepository {

    private val columns = Columns.raw("id,name,concentration,brands(name)")

    override suspend fun searchFragrances(query: String): List<FragranceSummary> =
        supabase.postgrest["fragrances"]
            .select(columns) {
                filter { ilike("name", "%$query%") }
                limit(10L)
            }
            .decodeList<FragranceRow>()
            .map { it.toSummary() }

    override suspend fun getTrending(limit: Int): List<FragranceSummary> =
        supabase.postgrest["fragrances"]
            .select(columns) {
                limit(limit.toLong())
            }
            .decodeList<FragranceRow>()
            .map { it.toSummary() }

    private fun FragranceRow.toSummary() = FragranceSummary(
        id = id,
        name = name,
        brandName = brands.name,
        concentration = concentration
    )
}

@Serializable
private data class FragranceRow(
    val id: Long,
    val name: String,
    val concentration: String? = null,
    val brands: BrandRow
)

@Serializable
private data class BrandRow(val name: String)

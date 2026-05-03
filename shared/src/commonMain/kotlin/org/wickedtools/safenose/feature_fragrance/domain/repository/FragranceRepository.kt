package org.wickedtools.safenose.feature_fragrance.domain.repository

import org.wickedtools.safenose.feature_fragrance.domain.model.FragranceSummary

interface FragranceRepository {
    suspend fun searchFragrances(query: String): List<FragranceSummary>
    suspend fun getTrending(limit: Int = 6): List<FragranceSummary>
}

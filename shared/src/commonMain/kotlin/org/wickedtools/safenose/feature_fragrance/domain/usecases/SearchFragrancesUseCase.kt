package org.wickedtools.safenose.feature_fragrance.domain.usecases

import org.wickedtools.safenose.feature_fragrance.domain.model.FragranceSummary
import org.wickedtools.safenose.feature_fragrance.domain.repository.FragranceRepository

class SearchFragrancesUseCase(private val repository: FragranceRepository) {
    suspend operator fun invoke(query: String): List<FragranceSummary> =
        if (query.length >= 2) repository.searchFragrances(query) else emptyList()
}

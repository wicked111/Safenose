package org.wickedtools.safenose.feature_fragrance.domain.usecases

import org.wickedtools.safenose.feature_fragrance.domain.model.FragranceSummary
import org.wickedtools.safenose.feature_fragrance.domain.repository.FragranceRepository

class GetTrendingFragrancesUseCase(private val repository: FragranceRepository) {
    suspend operator fun invoke(): List<FragranceSummary> = repository.getTrending()
}

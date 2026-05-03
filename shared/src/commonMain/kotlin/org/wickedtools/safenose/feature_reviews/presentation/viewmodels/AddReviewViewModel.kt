package org.wickedtools.safenose.feature_reviews.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.wickedtools.safenose.feature_authentication.domain.usecases.GetCurrentUserUseCase
import org.wickedtools.safenose.feature_fragrance.domain.model.FragranceSummary
import org.wickedtools.safenose.feature_fragrance.domain.usecases.GetTrendingFragrancesUseCase
import org.wickedtools.safenose.feature_fragrance.domain.usecases.SearchFragrancesUseCase
import org.wickedtools.safenose.feature_reviews.domain.usecases.SubmitReviewUseCase

class AddReviewViewModel(
    private val searchFragrancesUseCase: SearchFragrancesUseCase,
    private val getTrendingFragrancesUseCase: GetTrendingFragrancesUseCase,
    private val submitReviewUseCase: SubmitReviewUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddReviewUiState())
    val uiState: StateFlow<AddReviewUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val user = getCurrentUserUseCase()
                val trending = getTrendingFragrancesUseCase()
                _uiState.update { it.copy(city = user?.city ?: "", trendingFragrances = trending) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        if (query.length < 2) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            _uiState.update { it.copy(isSearching = true) }
            try {
                val results = searchFragrancesUseCase(query)
                _uiState.update { it.copy(searchResults = results, isSearching = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSearching = false, error = e.message) }
            }
        }
    }

    fun onFragranceSelected(fragrance: FragranceSummary) =
        _uiState.update { it.copy(selectedFragrance = fragrance) }

    fun onOverallRatingChanged(rating: Int) =
        _uiState.update { it.copy(overallRating = rating) }

    fun onLongevityRankChanged(rank: String) =
        _uiState.update { it.copy(longevityRank = rank) }

    fun onProjectionRankChanged(rank: String) =
        _uiState.update { it.copy(projectionRank = rank) }

    fun onValueRankChanged(rank: String) =
        _uiState.update { it.copy(valueRank = rank) }

    fun onSeasonSelected(season: String) =
        _uiState.update { it.copy(selectedSeason = season) }

    fun onOccasionToggled(occasion: String) =
        _uiState.update { state ->
            val updated = if (occasion in state.selectedOccasions)
                state.selectedOccasions - occasion
            else
                state.selectedOccasions + occasion
            state.copy(selectedOccasions = updated)
        }

    fun onReviewTextChanged(text: String) =
        _uiState.update { it.copy(reviewText = text) }

    fun onSubmit() {
        val state = _uiState.value
        val fragrance = state.selectedFragrance ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }
            try {
                submitReviewUseCase(
                    fragranceId = fragrance.id,
                    overallRating = state.overallRating,
                    longevityRank = state.longevityRank.ifBlank { null },
                    projectionRank = state.projectionRank.ifBlank { null },
                    valueRank = state.valueRank.ifBlank { null },
                    seasonWorn = state.selectedSeason.ifBlank { null },
                    reviewText = state.reviewText.ifBlank { null }
                )
                _uiState.update { it.copy(isSubmitting = false, isSubmitSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSubmitting = false, error = e.message ?: "Failed to submit review") }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }

    fun resetForm() = _uiState.update {
        AddReviewUiState(city = it.city, trendingFragrances = it.trendingFragrances)
    }
}

data class AddReviewUiState(
    val city: String = "",
    val searchQuery: String = "",
    val searchResults: List<FragranceSummary> = emptyList(),
    val trendingFragrances: List<FragranceSummary> = emptyList(),
    val isSearching: Boolean = false,
    val selectedFragrance: FragranceSummary? = null,
    val overallRating: Int = 0,
    val longevityRank: String = "",
    val projectionRank: String = "",
    val valueRank: String = "",
    val selectedSeason: String = "",
    val selectedOccasions: Set<String> = emptySet(),
    val reviewText: String = "",
    val isSubmitting: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val error: String? = null
)

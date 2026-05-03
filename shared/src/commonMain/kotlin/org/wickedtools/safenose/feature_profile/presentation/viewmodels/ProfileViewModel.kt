package org.wickedtools.safenose.feature_profile.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_profile.domain.usecases.GetProfileUseCase
import org.wickedtools.safenose.feature_profile.domain.usecases.UpdateProfileUseCase
import org.wickedtools.safenose.feature_reviews.domain.model.UserReview
import org.wickedtools.safenose.feature_reviews.domain.usecases.GetUserReviewsUseCase

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getUserReviewsUseCase: GetUserReviewsUseCase
) : ViewModel(){
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()


    init {
        loadProfile()
    }

    fun loadProfile(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = getProfileUseCase()
                _uiState.update { it.copy(user = user, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
        viewModelScope.launch {
            try {
                val reviews = getUserReviewsUseCase()
                _uiState.update { it.copy(reviews = reviews) }
            } catch (_: Exception) {}
        }
    }

    fun onUpdateClicked(username: String?, fullName: String?, city: String, avatarUrl: String?) {
        if (city.isBlank()) {
            _uiState.update { it.copy(error = "City is required") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = updateProfileUseCase(username, fullName, city, avatarUrl)
                _uiState.update { it.copy(isLoading = false, user = user, isUpdateSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
    fun clearSuccess() = _uiState.update { it.copy(isUpdateSuccess = false) }



}






data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val reviews: List<UserReview> = emptyList(),
    val error: String? = null,
    val isUpdateSuccess: Boolean = false
)
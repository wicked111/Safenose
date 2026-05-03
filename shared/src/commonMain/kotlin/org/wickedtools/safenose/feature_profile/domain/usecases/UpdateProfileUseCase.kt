package org.wickedtools.safenose.feature_profile.domain.usecases
import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_profile.domain.repository.ProfileRepository

class UpdateProfileUseCase (private val repository: ProfileRepository) {
    suspend operator fun invoke (username: String?, fullName: String?, city: String, avatarUrl: String?, ): User {
    return repository.updateProfile(username, fullName, city, avatarUrl)}
}
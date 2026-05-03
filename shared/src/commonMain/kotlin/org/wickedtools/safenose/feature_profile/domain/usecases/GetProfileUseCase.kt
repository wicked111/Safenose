package org.wickedtools.safenose.feature_profile.domain.usecases

import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_profile.domain.repository.ProfileRepository

class GetProfileUseCase (private val repository: ProfileRepository) {
    suspend operator fun invoke () : User?{
        return repository.getProfile()
    }
}

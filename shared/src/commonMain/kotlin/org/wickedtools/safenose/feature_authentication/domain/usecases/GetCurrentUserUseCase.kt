package org.wickedtools.safenose.feature_authentication.domain.usecases

import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_authentication.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): User? = repository.getCurrentUser()
}

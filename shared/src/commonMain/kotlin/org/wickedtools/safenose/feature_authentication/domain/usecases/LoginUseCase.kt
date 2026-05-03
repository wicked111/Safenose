package org.wickedtools.safenose.feature_authentication.domain.usecases

import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_authentication.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): User =
        repository.login(email, password)
}

package org.wickedtools.safenose.feature_authentication.domain.usecases

import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_authentication.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String,
        city: String
    ): User = repository.signUp(email, password, username, city)
}

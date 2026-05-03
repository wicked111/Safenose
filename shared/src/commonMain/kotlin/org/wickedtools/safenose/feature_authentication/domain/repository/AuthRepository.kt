package org.wickedtools.safenose.feature_authentication.domain.repository

import org.wickedtools.safenose.core.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
    suspend fun signUp(email: String, password: String, username: String, city: String): User
    suspend fun logout()
    suspend fun getCurrentUser(): User?
}

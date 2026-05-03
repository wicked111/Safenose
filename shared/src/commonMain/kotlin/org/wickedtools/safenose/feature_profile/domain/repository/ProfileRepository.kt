package org.wickedtools.safenose.feature_profile.domain.repository
import org.wickedtools.safenose.core.domain.model.User

interface ProfileRepository {
    suspend fun getProfile(): User?
    suspend fun updateProfile(username: String?, fullName: String?, city: String, avatarUrl: String?) : User

}
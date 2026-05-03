package org.wickedtools.safenose.feature_authentication.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_authentication.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    override suspend fun login(email: String, password: String): User {
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        return getCurrentUser() ?: error("Login succeeded but user profile not found")
    }

    override suspend fun signUp(
        email: String,
        password: String,
        username: String,
        city: String
    ): User {
        supabaseClient.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        val userId = supabaseClient.auth.currentUserOrNull()?.id
            ?: error("Sign up succeeded but no user session found")

        // Insert into public.profiles with the mandatory city field
        supabaseClient.postgrest["profiles"].insert(
            ProfileInsert(
                id = userId,
                username = username,
                city = city
            )
        )

        return getCurrentUser() ?: error("Sign up succeeded but profile not found")
    }

    override suspend fun logout() {
        supabaseClient.auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        val authUser = supabaseClient.auth.currentUserOrNull() ?: return null

        val profile = supabaseClient.postgrest["profiles"]
            .select {
                filter { eq("id", authUser.id) }
            }
            .decodeSingleOrNull<ProfileRow>() ?: return null

        return User(
            id = authUser.id,
            email = authUser.email ?: "",
            username = profile.username,
            fullName = profile.full_name,
            city = profile.city,
            state = profile.state,
            avatarUrl = profile.avatar_url
        )
    }
}

@Serializable
private data class ProfileInsert(
    val id: String,
    val username: String,
    val city: String
)

@Serializable
private data class ProfileRow(
    val id: String,
    val username: String? = null,
    val full_name: String? = null,
    val city: String,
    val state: String? = null,
    val avatar_url: String? = null
)

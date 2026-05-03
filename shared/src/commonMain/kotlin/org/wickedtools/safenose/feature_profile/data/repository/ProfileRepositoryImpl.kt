package org.wickedtools.safenose.feature_profile.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import org.wickedtools.safenose.core.domain.model.User
import org.wickedtools.safenose.feature_profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl (private val supabase : SupabaseClient) : ProfileRepository {

    override suspend fun getProfile(): User? {
        val loggedinuser = supabase.auth.currentUserOrNull() ?: return null

        val profile = supabase.postgrest["profiles"]
            .select {
                filter {
                    eq("id", loggedinuser.id)
                }
            }
            .decodeSingleOrNull<ProfileRow>() ?: return null


            return User(
                id = loggedinuser.id,
            email = loggedinuser.email ?: "",
                username = profile.username,
                fullName = profile.full_name,
                city = profile.city,
                state = profile.state,
                avatarUrl = profile.avatar_url,
            )
        }

    override suspend fun updateProfile(
        username: String?,
        fullName: String?,
        city: String,
        avatarUrl: String?
    ): User {

        val loggedinuserid = supabase.auth.currentUserOrNull()?.id

        supabase.postgrest["profiles"]
            .update({set("username", username)
                set("full_name", fullName)
                set("city", city)
                set("avatar_url", avatarUrl)
            }) {
                filter {eq("id", loggedinuserid!!)}
            }

        return getProfile() ?: error("Profile not found")
    }


}

@Serializable
private data class ProfileRow(
    val id: String,
    val username: String? = null,
    val full_name: String? = null,
    val city: String,
    val state: String? = null,
    val avatar_url: String? = null
)






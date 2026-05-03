package org.wickedtools.safenose.core.domain.model

data class User(
    val id: String,
    val email: String,
    val username: String?,
    val fullName: String?,
    val city: String,
    val state: String?,
    val avatarUrl: String?
)

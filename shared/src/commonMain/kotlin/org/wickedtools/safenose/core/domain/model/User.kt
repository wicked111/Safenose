package org.wickedtools.safenose.core.domain.model

data class User(
    val name : String,
    val age : Int,
    val email: String?,
    val city: String,
    val phone: Long?
)

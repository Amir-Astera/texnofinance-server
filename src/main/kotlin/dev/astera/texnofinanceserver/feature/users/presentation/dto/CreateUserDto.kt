package dev.astera.texnofinanceserver.feature.users.presentation.dto


data class CreateUserDto(
    val name: String,
    val login: String?,
    val surname: String?,
    val email: String?,
    val phone: String?,
    val authorityIds: List<String>?,
    val partnerIds: List<String>?
)
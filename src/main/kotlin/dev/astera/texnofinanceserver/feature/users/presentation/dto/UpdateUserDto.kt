package dev.astera.texnofinanceserver.feature.users.presentation.dto


data class UpdateUserDto (
    val name: String?,
    val surname: String?,
    val email: String?,
    val phone: String?,
    val authorityIds: List<String>?,
    val partnerIds: List<String>?
)
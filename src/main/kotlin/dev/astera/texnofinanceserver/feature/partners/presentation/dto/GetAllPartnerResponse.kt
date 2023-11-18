package dev.astera.texnofinanceserver.feature.partners.presentation.dto

import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner

data class GetAllPartnerResponse(
    val id: String,
    val name: String,
    val description: String?,
    val status: Partner.PartnerStatus
)
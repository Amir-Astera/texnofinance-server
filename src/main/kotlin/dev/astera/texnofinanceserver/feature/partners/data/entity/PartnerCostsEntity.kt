package dev.astera.texnofinanceserver.feature.partners.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "partner_cost")
data class PartnerCostsEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val partnerId: String,
    val costId: String,
    @Version
    val version: Long? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime
)

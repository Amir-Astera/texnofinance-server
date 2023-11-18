package dev.astera.texnofinanceserver.feature.partners.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "user_partner")
data class UserPartnerEntity(
        @Id
        val id: String,
        val userId: String,
        val partnerId: String,
        @Version
        val version: Long? = null,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime
)
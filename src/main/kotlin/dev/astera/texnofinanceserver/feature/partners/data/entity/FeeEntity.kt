package dev.astera.texnofinanceserver.feature.partners.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "fee")
data class FeeEntity(
    @Id
    val id: String,
    val fee: Double,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

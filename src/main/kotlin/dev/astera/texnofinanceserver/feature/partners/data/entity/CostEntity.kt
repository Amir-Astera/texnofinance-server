package dev.astera.texnofinanceserver.feature.partners.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "cost")
data class CostEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val cost: Double = 0.0,
    val stableCost: Double = 0.0,
    val month: LocalDateTime,
    @Version
    val version: Long? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

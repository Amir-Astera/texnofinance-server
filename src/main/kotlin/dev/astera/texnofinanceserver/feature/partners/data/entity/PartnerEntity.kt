package dev.astera.texnofinanceserver.feature.partners.data.entity

import dev.astera.texnofinanceserver.feature.reports.data.entity.ReportEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "partner")
data class PartnerEntity(
        @Id
        val id: String,
        val name: String,
        val description: String?,
        val status: Short,
        val logo: String?,
        @Version
        val version: Long? = null,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime = LocalDateTime.now()
)
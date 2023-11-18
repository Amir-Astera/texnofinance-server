package dev.astera.texnofinanceserver.feature.reports.data.entity

import dev.astera.texnofinanceserver.feature.partners.data.entity.PartnerEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "partner_report")
data class PartnerReportEntity(
        @Id
        val id: String,
        val reportId: String,
        val partnerId: String,
        @Version
        val version: Long?,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime
)

package dev.astera.texnofinanceserver.feature.reports.data.entity

import dev.astera.texnofinanceserver.feature.partners.data.entity.PartnerEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "reports")
data class ReportEntity(
        @Id
        val id: String,
        val reportDate: LocalDateTime,
        val profit: Double,
        val profitStore: Double,
        val otherProfit: Double,
        val clients: Int,
        val newClients: Int,
        val netIssue: Double,
        val cost: Double,
        val invoice: Double,
        val auctioneerEquipment: Double,
        val auctioneerCoat: Double,
        val cashInDeposit: Double,
        val note: String,
        val auctioneerGold: Double?,
        val cashInDepositGold: Double?,
        @Version
        val version: Long?,
        val createdAt: LocalDateTime
)

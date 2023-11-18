package dev.astera.texnofinanceserver.feature.reports.domain.models

import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import org.springframework.data.annotation.Version
import java.time.LocalDateTime
import java.util.UUID

data class ReportAggregate(
        val id: String = UUID.randomUUID().toString(),
        val reportDate: LocalDateTime,
        val profit: Double,
        val profitStore: Double,
        val otherProfit: Double = 0.0,
        val clients: Int,
        val newClients: Int,
        val netIssue: Double,
        val cost: Double,
        val invoice: Double,
        val auctioneerEquipment: Double,
        val auctioneerCoat: Double,
        val cashInDeposit: Double,
        val note: String,
        val auctioneerGold: Double? = 0.0,
        val cashInDepositGold: Double? = 0.0,
        val version: Long? = null,
        val createdAt: LocalDateTime? = LocalDateTime.now()
)

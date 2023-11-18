package dev.astera.texnofinanceserver.feature.reports.domain.models

import org.springframework.data.annotation.Version
import java.time.LocalDateTime

data class FinalReport(
    val profit: Double,
    val profitStore: Double,
    val otherProfit: Double,
    val profits: Double,
    val costs: Double,
    val netProfit: Double,
    val clients: Int,
    val newClients: Int,
    val netIssue: Double,
    val auctioneerEquipment: Double,
    val auctioneerCoat: Double,
    val cashInDeposit: Double,
)

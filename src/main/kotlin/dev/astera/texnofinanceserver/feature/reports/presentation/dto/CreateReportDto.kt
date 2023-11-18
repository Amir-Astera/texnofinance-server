package dev.astera.texnofinanceserver.feature.reports.presentation.dto

import java.time.LocalDateTime

data class CreateReportDto(
        val reportDate: LocalDateTime,
        val profit: Double,
        val profitStore: Double,
        val otherProfit: Double,
        val clients: Int,
        val newClients: Int,
        val netIssue: Double,// (выдача за сутки - она в формуле считается. в ентити никуда не прибавляется) чистая выдача за сутки - возврат - накладная
        val cost: Double,
        val invoice: Double,
        val auctioneerEquipment: Double,
        val auctioneerCoat: Double,
        val cashInDeposit: Double,
        val note: String,
        val auctioneerGold: Double?,
        val cashInDepositGold: Double?
)
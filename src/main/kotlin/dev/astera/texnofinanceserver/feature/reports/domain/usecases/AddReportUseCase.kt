package dev.astera.texnofinanceserver.feature.reports.domain.usecases

import dev.astera.texnofinanceserver.core.config.api.CreateResponseDto
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.reports.domain.models.ReportAggregate
import dev.astera.texnofinanceserver.feature.reports.domain.services.ReportsService
import dev.astera.texnofinanceserver.feature.reports.presentation.dto.CreateReportDto
import org.springframework.stereotype.Component
import java.time.LocalDateTime

interface AddReportUseCase {
    suspend operator fun invoke(partnerId: String, dto: CreateReportDto): CreateResponseDto
}

@Component
internal class AddReportUseCaseImpl(
        private val reportsService: ReportsService,
        private val partnerService: PartnerService
): AddReportUseCase {
    override suspend fun invoke(partnerId: String, dto: CreateReportDto): CreateResponseDto {
        val partnerAggregate = partnerService.get(partnerId)
        val reportAggregate = dto.run {
            ReportAggregate(
                    reportDate = reportDate,
                    profit = profit,
                    profitStore = profitStore,
                    otherProfit = otherProfit,
                    clients = clients,
                    newClients = newClients,
                    netIssue = netIssue,
                    cost = cost,
                    invoice = invoice,
                    auctioneerEquipment = auctioneerEquipment,
                    auctioneerCoat = auctioneerCoat,
                    cashInDeposit = cashInDeposit,
                    note = note,
                    auctioneerGold = auctioneerGold,
                    cashInDepositGold = cashInDepositGold,
                    createdAt = LocalDateTime.now()
            )
        }
        reportsService.save(reportAggregate, partnerAggregate.id)
        return CreateResponseDto(reportAggregate.id)
    }

}
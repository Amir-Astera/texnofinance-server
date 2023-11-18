package dev.astera.texnofinanceserver.feature.reports.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.reports.domain.models.FinalReport
import dev.astera.texnofinanceserver.feature.reports.domain.services.ReportsService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.pow

interface GetReportsUseCase {
    suspend operator fun invoke(partnerId: String, fromDate: LocalDateTime, toDate: LocalDateTime): FinalReport
}

@Service
internal class GetReportsUseCaseImpl(
    private val reportsService: ReportsService,
    private val partnerService: PartnerService
): GetReportsUseCase {
    override suspend fun invoke(partnerId: String, fromDate: LocalDateTime, toDate: LocalDateTime): FinalReport {
        val reports = reportsService.getReports(partnerId, fromDate, toDate)
        val fee = partnerService.getFee()
        val profit = reports.sumOf { it.profit }
        val profitStore = reports.sumOf { it.profitStore }
        val otherProfits = reports.sumOf { it.otherProfit }
        val profits = profit + profitStore + otherProfits
        val costs = reports.sumOf { it.cost }
        val net = profits - costs
        val netProfit = net - ((fee / 100) * net)
        val newClients = reports.sumOf { it.newClients }
        val clients = reports.sumOf { it.clients } + newClients
        val invoice = reports.sumOf { it.invoice }
        val netIssue = reports.sumOf { it.netIssue } - costs - invoice
        return FinalReport(
            profit = profit,
            profitStore = profitStore,
            otherProfit = otherProfits,
            profits = profits, // доход + доход магазина + доп доходы
            costs = costs,
            netProfit = netProfit, //доходы - расходы - 3%
            clients = clients , //клиенты + новые клиенты(все клиенты)
            newClients = newClients,
            netIssue = netIssue, // выдача за сутки - возврат - накладная
            auctioneerEquipment = reports.sortedBy { it.reportDate }.last().auctioneerEquipment, // не суммируется, а берется из последнего отчета
            auctioneerCoat = reports.sortedBy { it.reportDate }.last().auctioneerCoat, // не суммируется, а берется из последнего отчета
            cashInDeposit = reports.sortedBy { it.reportDate }.last().cashInDeposit // не суммируется, а берется из последнего отчета
        )
    }
//накладная отдельно
    //выдача за сеголня 183а
}
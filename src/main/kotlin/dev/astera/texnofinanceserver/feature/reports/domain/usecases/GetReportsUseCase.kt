package dev.astera.texnofinanceserver.feature.reports.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.reports.domain.models.FinalReport
import dev.astera.texnofinanceserver.feature.reports.domain.services.ReportsService
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

interface GetReportsUseCase {
    suspend operator fun invoke(partnerId: String, fromDate: LocalDateTime, toDate: LocalDateTime): FinalReport
}

@Service
internal class GetReportsUseCaseImpl(
    private val reportsService: ReportsService,
    private val partnerService: PartnerService
): GetReportsUseCase {
    override suspend fun invoke(partnerId: String, fromDate: LocalDateTime, toDate: LocalDateTime): FinalReport {
        val partner = partnerService.get(partnerId)
        val reports = reportsService.getReports(partner.id, fromDate, toDate)
        val fee = partnerService.getFee()
        val costsEntity = partnerService.getPartnerCosts(partner.id, fromDate, toDate)
        val stableCost = partnerService.getPartnerStableCost(partner.id)
        val monthDays = LocalDateTime.now().month.maxLength()
        val days = abs(Duration.between(toDate.toLocalDate().atStartOfDay(), fromDate.toLocalDate().atStartOfDay()).toDays()).toInt()


        val profit = reports.sumOf { it.profit }
        val profitStore = reports.sumOf { it.profitStore }
        val otherProfits = reports.sumOf { it.otherProfit }
        val profits = profit + profitStore + otherProfits
        val costs = costsEntity.sumOf { it.cost } + stableCost// Расход
        val refund = reports.sumOf { it.cost } // Возврат
        val net = profits - ((costs / monthDays) * if (days == 29) days + 1 else days)
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
            costs = costs, //возврат
            netProfit = (netProfit * 100.0).roundToInt() / 100.0, //доходы - расходы - 3% //Math.round(number3digits * 100.0) / 100.0
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
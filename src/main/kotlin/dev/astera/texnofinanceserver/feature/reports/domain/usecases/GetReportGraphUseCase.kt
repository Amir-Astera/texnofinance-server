package dev.astera.texnofinanceserver.feature.reports.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.reports.domain.models.FinalReport
import dev.astera.texnofinanceserver.feature.reports.domain.models.Graph
import dev.astera.texnofinanceserver.feature.reports.domain.services.ReportsService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.Month

interface GetReportGraphUseCase {
    suspend operator fun invoke(partnerId: String): List<Graph>
}

@Service
internal class GetReportGraphUseCaseImpl(
    private val reportsService: ReportsService,
    private val partnerService: PartnerService
): GetReportGraphUseCase {
    override suspend fun invoke(partnerId: String): List<Graph> {
        val reports = reportsService.getAllReports(partnerId)
        val fee = partnerService.getFee()
        val reportsByDate = reports.groupBy { it.reportDate.toLocalDate().month }
        val date = reportsByDate.mapValues { report ->
            report.value.sumOf { it.profit + it.profitStore } - (fee / 100)
        }
        val graph = date.entries.run {
            map {
                Graph(
                    name = it.key,
                    value = it.value
                )
            }
        }
        return graph
    }

}
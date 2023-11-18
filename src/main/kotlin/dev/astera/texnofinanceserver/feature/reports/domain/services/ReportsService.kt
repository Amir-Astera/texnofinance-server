package dev.astera.texnofinanceserver.feature.reports.domain.services

import dev.astera.texnofinanceserver.core.extension.toEntity
import dev.astera.texnofinanceserver.core.extension.toModel
import dev.astera.texnofinanceserver.feature.partners.domain.errors.PartnerNotFoundException
import dev.astera.texnofinanceserver.feature.reports.data.entity.PartnerReportEntity
import dev.astera.texnofinanceserver.feature.reports.domain.models.FinalReport
import dev.astera.texnofinanceserver.repositories.PartnerReportRepository
import dev.astera.texnofinanceserver.repositories.ReportRepository
import dev.astera.texnofinanceserver.feature.reports.domain.models.ReportAggregate
import dev.astera.texnofinanceserver.repositories.PartnerRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.flux
import org.springframework.stereotype.Service
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator
import java.time.LocalDateTime

interface ReportsService {
    suspend fun save(report: ReportAggregate, partnerId: String)
    suspend fun getReports(partnerId: String, fromDate: LocalDateTime, toDate: LocalDateTime): Collection<ReportAggregate>
    suspend fun getAllReports(partnerId: String): Collection<ReportAggregate>
}

@Service
internal class ReportsServiceImpl(
    private val transactionManager: ReactiveTransactionManager,
    private val reportRepository: ReportRepository,
    private val partnerReportRepository: PartnerReportRepository,
    private val partnerRepository: PartnerRepository
): ReportsService {
    override suspend fun save(report: ReportAggregate, partnerId: String) {
        val operator = TransactionalOperator.create(transactionManager)
        reportRepository.saveAll(listOf(report.toEntity())).asFlux()
            .thenMany(
                partnerReportRepository.saveAll(
                    listOf(
                        report.run {
                            val id = "${report.id}-${partnerId}"
                            PartnerReportEntity(id, report.id, partnerId, version, reportDate, reportDate)
                        }
                    )
            ).asFlux()
            ).`as`(operator::transactional).asFlow().collect {}
    }

    override suspend fun getReports(partnerId: String, fromDate: LocalDateTime, toDate: LocalDateTime): Collection<ReportAggregate> {
        val partner = partnerRepository.findById(partnerId) ?: throw PartnerNotFoundException()
        return partnerReportRepository.findAllByPartnerId(partner.id).run {
            reportRepository.findAllByIdAndReportDate(fromDate, toDate, map { it.reportId }.toList()).map { report ->
                report.toModel()
            }
        }.toList()
    }

    override suspend fun getAllReports(partnerId: String): Collection<ReportAggregate> {
        val partner = partnerRepository.findById(partnerId) ?: throw PartnerNotFoundException()
        return partnerReportRepository.findAllByPartnerId(partner.id).run {
            reportRepository.findAllById(map { it.reportId }).map { report ->
                report.toModel()
            }
        }.toList()
    }

}
package dev.astera.texnofinanceserver.feature.partners.domain.services

import dev.astera.texnofinanceserver.core.extension.toEntity
import dev.astera.texnofinanceserver.core.extension.toModel
import dev.astera.texnofinanceserver.feature.partners.data.entity.CostEntity
import dev.astera.texnofinanceserver.feature.partners.data.entity.PartnerCostsEntity
import dev.astera.texnofinanceserver.feature.partners.domain.errors.PartnerNotFoundException
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.reports.domain.models.ReportAggregate
import dev.astera.texnofinanceserver.repositories.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.cglib.core.Local
import org.springframework.data.annotation.Id
import org.springframework.stereotype.Component
import java.time.LocalDateTime

interface PartnerService {
    suspend fun get(id: String): Partner
    suspend fun getAll(): Collection<Partner>
    suspend fun save(aggregate: Partner)
    suspend fun delete(id: String)
    suspend fun findByName(name: String): Partner?
    suspend fun getAllByIds(partnerIds: List<String>): Collection<Partner>
    suspend fun getFee(): Double
    suspend fun addPartnerCost(partnerId: String, count: Double? = null, stableCost: Double? = null, day: LocalDateTime): Unit
    suspend fun addCost(count: Double, month: LocalDateTime): Unit
    suspend fun getPartnerCosts(partnerId: String, fromDate: LocalDateTime, toDate: LocalDateTime): List<CostEntity>
    suspend fun getPartnerStableCost(partnerId: String): Double
}

@Component
class PartnerServiceImpl(
    private val partnerRepository: PartnerRepository,
    private val partnerReportRepository: PartnerReportRepository,
    private val reportRepository: ReportRepository,
    private val feeRepository: FeeRepository,
    private val costsRepository: CostsRepository,
    private val partnerCostsRepository: PartnerCostsRepository
): PartnerService{
    override suspend fun get(id: String): Partner =
            partnerRepository.findById(id)?.run { toModel(reports = getReports(id)) } ?: throw PartnerNotFoundException()

    override suspend fun getAll(): Collection<Partner> =
            partnerRepository.findAllVisible().map { it.toModel(reports = getReports(it.id)) }.toList()

    override suspend fun save(aggregate: Partner) {
        partnerRepository.save(aggregate.toEntity())
    }

    override suspend fun delete(id: String) = partnerRepository.deleteById(id)


    override suspend fun findByName(name: String): Partner? =
            partnerRepository.findByName(name)?.run { toModel(reports = getReports(id)) }

    override suspend fun getAllByIds(partnerIds: List<String>): Collection<Partner> {
        return partnerRepository.findAllVisibleByIds(partnerIds).map {
            it.toModel(reports = getReports(it.id))
        }.toList()
    }

    override suspend fun getFee(): Double {
        return feeRepository.findById("fee")?.run { fee } ?: 0.0
    }

    private suspend fun getReports(partnerId: String): Collection<ReportAggregate> {
        return partnerReportRepository.findAllByPartnerId(partnerId)?.run {
            reportRepository.findAllById(map { it.reportId }).map { report ->
                report.toModel()
            }
        }?.toList() ?: listOf(ReportAggregate(
            "",
            LocalDateTime.now(),
            0.0,
            0.0,
            0.0,
            0,
            0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            "",
            0.0
        ))
    }

    override suspend fun addPartnerCost(partnerId: String, count: Double?, stableCost: Double?, day: LocalDateTime) {
        val partnerEntity = partnerRepository.findById(partnerId) ?: throw PartnerNotFoundException()
        val constEntity = costsRepository.save(
            CostEntity(
                cost = count ?: 0.0,
                stableCost = stableCost ?: 0.0,
                month = day,
                createdAt = LocalDateTime.now()
            )
        )
        partnerCostsRepository.save(PartnerCostsEntity(
            partnerId = partnerEntity.id,
            costId = constEntity.id,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ))
    }

    override suspend fun addCost(count: Double, month: LocalDateTime) {
        costsRepository.save(
            CostEntity(
                cost = count,
                month = month,
                createdAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun getPartnerCosts(partnerId: String, fromDate: LocalDateTime, toDate: LocalDateTime): List<CostEntity> {
        val partnerCostsEntity = partnerCostsRepository.findAllByPartnerId(partnerId)?.map { it }
        val costEntity = costsRepository.findAllByIdAndMonth(fromDate, toDate, partnerCostsEntity!!.map { it.costId }.toList())
        return costEntity.toList()
    }

    override suspend fun getPartnerStableCost(partnerId: String): Double {
        val partnerCostsEntity = partnerCostsRepository.findAllByPartnerId(partnerId)
        val costsEntity = costsRepository.findAllById(partnerCostsEntity!!.map { it.costId })
        return costsEntity.toList().sumOf { it.stableCost }
    }

}
package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.reports.data.entity.PartnerReportEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PartnerReportRepository: CoroutineCrudRepository<PartnerReportEntity, String> {
    fun findAllByPartnerId(partnerId: String): Flow<PartnerReportEntity>
}
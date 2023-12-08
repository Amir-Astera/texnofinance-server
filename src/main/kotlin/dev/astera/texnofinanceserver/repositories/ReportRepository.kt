package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.reports.data.entity.ReportEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ReportRepository: CoroutineCrudRepository<ReportEntity, String> {
    @Query("select * from reports where id in(:reportIds) and report_date BETWEEN :fromDate and :toDate")
    fun findAllByIdAndReportDate(fromDate: LocalDateTime, toDate: LocalDateTime, reportIds: Collection<String>): Flow<ReportEntity>
    override fun findAllById(id: Flow<String>): Flow<ReportEntity>
}
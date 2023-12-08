package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.partners.data.entity.CostEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime

interface CostsRepository: CoroutineCrudRepository<CostEntity, String> {
        @Query("select * from cost where id in(:costsId) and month BETWEEN :fromDate and :toDate")
        fun findAllByIdAndMonth(fromDate: LocalDateTime, toDate: LocalDateTime, costsId: Collection<String>): Flow<CostEntity>
}
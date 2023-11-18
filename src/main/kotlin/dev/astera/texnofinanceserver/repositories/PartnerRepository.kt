package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.partners.data.entity.PartnerEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PartnerRepository : CoroutineCrudRepository<PartnerEntity, String> {
    @Query("select * from partner v where v.status in (1, 2)")
    fun findAllVisible(): Flow<PartnerEntity>

    suspend fun findByName(name: String): PartnerEntity?

    @Query("select * from partner where id in (:partnerIds) and status in(1,2)")
    suspend fun findAllVisibleByIds(partnerIds: List<String>): Flow<PartnerEntity>
}
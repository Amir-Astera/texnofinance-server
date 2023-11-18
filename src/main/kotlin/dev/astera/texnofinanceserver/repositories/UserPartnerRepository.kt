package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.partners.data.entity.UserPartnerEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPartnerRepository : CoroutineCrudRepository<UserPartnerEntity, String> {
    fun findAllByUserId(userId: String): Flow<UserPartnerEntity>

    @Query("delete from user_partner v where v.user_id = :userId and v.partner_id not in (:partnerIds)")
    suspend fun deleteAllByUserIdAndPartnerIdNotIn(userId: String, partnerIds: List<String>)

    @Query("delete from user_partner v where v.user_id = :userId")
    suspend fun deleteAllByUserId(userId: String)
}
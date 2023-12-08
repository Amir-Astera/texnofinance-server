package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.partners.data.entity.PartnerCostsEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PartnerCostsRepository: CoroutineCrudRepository<PartnerCostsEntity, String> {
    fun findAllByPartnerId(partnerId: String): Flow<PartnerCostsEntity>?
}
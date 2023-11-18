package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.partners.data.entity.FeeEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FeeRepository: CoroutineCrudRepository<FeeEntity, String>
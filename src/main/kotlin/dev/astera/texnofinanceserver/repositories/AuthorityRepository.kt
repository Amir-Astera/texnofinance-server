package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.authority.data.entity.AuthorityEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository : CoroutineCrudRepository<AuthorityEntity, String> {
    suspend fun findByName(name: String): AuthorityEntity?
}

package dev.astera.texnofinanceserver.repositories

import dev.astera.texnofinanceserver.feature.users.data.entity.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CoroutineCrudRepository<UserEntity, String> {
    suspend fun findByLogin(login: String): UserEntity?

    suspend fun findByEmail(email: String): UserEntity?

    suspend fun findByPhone(phone: String): UserEntity?
}
package dev.astera.texnofinanceserver.feature.users.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "system_user")
class UserEntity(
        @Id
        val id: String,
        val name: String,
        val surname: String?,
        val email: String?,
        val phone: String?,
        val login: String,
        val logo: String?,
        @Version
        var version: Long?,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime
)

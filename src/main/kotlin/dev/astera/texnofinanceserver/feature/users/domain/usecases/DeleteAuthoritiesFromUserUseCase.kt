package dev.astera.texnofinanceserver.feature.users.domain.usecases

import dev.astera.texnofinanceserver.feature.authority.domain.errors.AuthorityNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import dev.astera.texnofinanceserver.feature.users.presentation.dto.DeleteAuthoritiesFromUserDto
import org.springframework.stereotype.Service

interface DeleteAuthoritiesFromUserUseCase {
    suspend operator fun invoke(userId: String, dto: DeleteAuthoritiesFromUserDto)
}

@Service
internal class DeleteAuthoritiesFromUserUseCaseImpl(
        private val userService: UserAggregateService,
) : DeleteAuthoritiesFromUserUseCase {
    override suspend fun invoke(userId: String, dto: DeleteAuthoritiesFromUserDto) {
            val user = userService.get(userId)
            val authorityIds = dto.authorityIds
            val authorities = userService.getAuthorities(authorityIds)
            if (authorities.size != authorityIds.size) {
                throw AuthorityNotFoundException()
            }
            authorities.forEach { authority->
                user.deleteAuthority(authority.id)
            }
            userService.save(user)
    }
}
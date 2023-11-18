package dev.astera.texnofinanceserver.feature.users.domain.usecases

import dev.astera.texnofinanceserver.feature.authority.domain.errors.AuthorityNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import dev.astera.texnofinanceserver.feature.users.presentation.dto.AddAuthoritiesToUserDto
import org.springframework.stereotype.Service

interface AddAuthoritiesToUserUseCase {
    suspend operator fun invoke(userId: String, dto: AddAuthoritiesToUserDto)
}

@Service
internal class AddAuthoritiesToUserUseCaseImpl(
        private val userService: UserAggregateService,
) : AddAuthoritiesToUserUseCase {
    override suspend fun invoke(userId: String, dto: AddAuthoritiesToUserDto) {
        val user = userService.get(userId)

        val authorityIds = dto.authorityIds
        val authorities = userService.getAuthorities(authorityIds)
        if (authorities.size != authorityIds.size) {
            throw AuthorityNotFoundException()
        }
        authorities.forEach { authority ->
            user.addAuthority(authority.id)
        }
        return userService.save(user)
    }
}
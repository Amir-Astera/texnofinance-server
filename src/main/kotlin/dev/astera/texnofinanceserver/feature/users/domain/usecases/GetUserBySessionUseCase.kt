package dev.astera.texnofinanceserver.feature.users.domain.usecases

import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.feature.users.domain.errors.UserNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.models.UserAggregate
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import org.springframework.stereotype.Service

interface GetUserBySessionUseCase {
	suspend operator fun invoke(sessionUser: SessionUser): UserAggregate
}

@Service
internal class GetUserBySessionUseCaseImpl(
	private val userService: UserAggregateService
) : GetUserBySessionUseCase {

	override suspend fun invoke(sessionUser: SessionUser): UserAggregate =
			userService.getByLogin(sessionUser.login) ?: throw UserNotFoundException()
}

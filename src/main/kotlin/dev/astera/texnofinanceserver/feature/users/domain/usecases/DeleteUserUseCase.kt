package dev.astera.texnofinanceserver.feature.users.domain.usecases

import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import org.springframework.stereotype.Service

interface DeleteUserUseCase {
    suspend operator fun invoke(id: String)
}

@Service
internal class DeleteUserUseCaseImpl(
    private val service: UserAggregateService
) : DeleteUserUseCase {
    override suspend fun invoke(id: String) = service.delete(id)
}
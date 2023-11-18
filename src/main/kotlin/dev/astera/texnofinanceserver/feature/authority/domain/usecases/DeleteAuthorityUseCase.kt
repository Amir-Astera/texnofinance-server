package dev.astera.texnofinanceserver.feature.authority.domain.usecases

import dev.astera.texnofinanceserver.feature.authority.domain.services.AuthorityAggregateService
import org.springframework.stereotype.Service

interface DeleteAuthorityUseCase {
    suspend operator fun invoke(id: String)
}

@Service
internal class DeleteAuthorityUseCaseImpl(
    private val service: AuthorityAggregateService
) : DeleteAuthorityUseCase {
    override suspend fun invoke(id: String) = service.delete(id)
}
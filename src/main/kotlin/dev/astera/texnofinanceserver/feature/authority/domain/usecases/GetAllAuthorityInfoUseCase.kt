package dev.astera.texnofinanceserver.feature.authority.domain.usecases

import dev.astera.texnofinanceserver.feature.authority.domain.models.Authority
import dev.astera.texnofinanceserver.feature.authority.domain.services.AuthorityAggregateService
import org.springframework.stereotype.Service

interface GetAllAuthorityInfoUseCase {
    suspend operator fun invoke(): Collection<Authority>
}

@Service
internal class GetAllAuthorityInfoUseCaseImpl(
    private val service: AuthorityAggregateService
) : GetAllAuthorityInfoUseCase {
    override suspend fun invoke(): Collection<Authority> = service.getAll()
}
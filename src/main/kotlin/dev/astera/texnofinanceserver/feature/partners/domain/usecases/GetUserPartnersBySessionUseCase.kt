package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.users.domain.errors.UserNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.errors.UserPartnersNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import org.springframework.stereotype.Service

interface GetUserPartnersBySessionUseCase {
    suspend operator fun invoke(sessionUser: SessionUser): Collection<Partner>
}

@Service
internal class GetUserPartnersBySessionUseCaseImpl(
    private val userAggregateService: UserAggregateService
) : GetUserPartnersBySessionUseCase {
    override suspend fun invoke(sessionUser: SessionUser): Collection<Partner> {
        val user = userAggregateService.getByLogin(sessionUser.login) ?: throw UserNotFoundException()
        val hasAdminAuthority = user.checkAdminAuthority()
        return if (hasAdminAuthority) {
            emptyList()
        } else {
            if (user.partners.isEmpty()) {
                throw UserPartnersNotFoundException()
            }
            user.partners
        }
    }
}

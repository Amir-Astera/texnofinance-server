package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.feature.partners.domain.errors.UserPartnerNotFoundException
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import org.springframework.stereotype.Service

interface GetPartnerUseCase {
	suspend operator fun invoke(partnerId: String, sessionUser: SessionUser): Partner
}

@Service
internal class GetPartnerUseCaseImpl(
		private val service: PartnerService,
		private val getUserPartnersBySessionUseCase: GetUserPartnersBySessionUseCase
) : GetPartnerUseCase {
	override suspend fun invoke(partnerId: String, sessionUser: SessionUser): Partner {
		val userPartners = getUserPartnersBySessionUseCase(sessionUser)
		val isAdmin = userPartners.isEmpty()
		if (!isAdmin) {
			userPartners.find { it.id == partnerId }
				?: throw UserPartnerNotFoundException(("Partner with id:${partnerId} not found for this user"))
		}
		return service.get(partnerId)
	}
}

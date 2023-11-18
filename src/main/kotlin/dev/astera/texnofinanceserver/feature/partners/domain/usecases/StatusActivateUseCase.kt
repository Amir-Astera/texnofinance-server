package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import org.springframework.stereotype.Service

interface StatusActivateUseCase {
	suspend operator fun invoke(id: String): Partner
}

@Service
internal class StatusActivateUseCaseImpl(
	private val service: PartnerService
) : StatusActivateUseCase {
	override suspend fun invoke(id: String): Partner {
		val aggregate = service.get(id)
		aggregate.updateStatus(Partner.PartnerStatus.ACTIVE)
		service.save(aggregate)
		return aggregate
	}
}

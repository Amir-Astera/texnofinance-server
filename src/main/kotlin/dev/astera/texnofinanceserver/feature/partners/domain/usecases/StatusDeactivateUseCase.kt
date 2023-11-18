package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import org.springframework.stereotype.Service


interface StatusDeactivateUseCase {
	suspend operator fun invoke(id: String): Partner
}

@Service
internal class StatusDeactivateUseCaseImpl(
	private val service: PartnerService
) : StatusDeactivateUseCase {
	override suspend fun invoke(id: String): Partner {
		val aggregate = service.get(id)
		aggregate.updateStatus(Partner.PartnerStatus.INACTIVE)
		service.save(aggregate)
		return aggregate
	}
}

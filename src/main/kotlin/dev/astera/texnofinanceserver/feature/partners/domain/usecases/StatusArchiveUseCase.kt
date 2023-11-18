package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import org.springframework.stereotype.Service


interface StatusArchiveUseCase {
	suspend operator fun invoke(id: String): Partner
}

@Service
internal class StatusArchiveUseCaseImpl(
	private val service: PartnerService
) : StatusArchiveUseCase {
	override suspend fun invoke(id: String): Partner {
		val aggregate = service.get(id)
		aggregate.updateStatus(Partner.PartnerStatus.ARCHIVED)
		service.save(aggregate)
		return aggregate
	}
}

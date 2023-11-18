package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.partners.presentation.dto.GetAllPartnerResponse
import org.springframework.stereotype.Service

interface GetAllPartnerInfoUseCase {
	suspend operator fun invoke(sessionUser: SessionUser): Collection<GetAllPartnerResponse>
}

@Service
internal class GetAllPartnerInfoUseCaseImpl(
		private val service: PartnerService,
		private val getUserPartnersBySessionUseCase: GetUserPartnersBySessionUseCase
) : GetAllPartnerInfoUseCase {
	override suspend fun invoke(sessionUser: SessionUser): Collection<GetAllPartnerResponse> {
		val userPartners = getUserPartnersBySessionUseCase(sessionUser)
		val isAdmin = userPartners.isEmpty()
		return if (isAdmin) {
			service.getAll().map {
				GetAllPartnerResponse(
					it.id,
					it.name,
					it.description,
					it.status
				)
			}
		} else {
			service.getAllByIds(userPartners.map { it.id }).map {
				GetAllPartnerResponse(
					it.id,
					it.name,
					it.description,
					it.status
				)
			}
		}
	}
}

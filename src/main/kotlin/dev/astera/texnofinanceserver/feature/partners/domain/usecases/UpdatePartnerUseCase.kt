package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.feature.partners.domain.errors.PartnerDuplicateNameException
import dev.astera.texnofinanceserver.feature.partners.domain.errors.UserPartnerNotFoundException
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.partners.presentation.dto.UpdatePartnerDto
import org.springframework.stereotype.Service

interface UpdatePartnerUseCase {
    suspend operator fun invoke(partnerId: String, partnerDto: UpdatePartnerDto, sessionUser: SessionUser): String
}

@Service
internal class UpdatePartnerUseCaseImpl(
        private val service: PartnerService,
        private val getUserPartnersBySessionUseCase: GetUserPartnersBySessionUseCase
) : UpdatePartnerUseCase {
    override suspend fun invoke(partnerId: String, partnerDto: UpdatePartnerDto, sessionUser: SessionUser): String {
        val userPartners = getUserPartnersBySessionUseCase(sessionUser)
        val isAdmin = userPartners.isEmpty()
        if (!isAdmin) {
            userPartners.find { it.id == partnerId }
                ?: throw UserPartnerNotFoundException(("Partner with id:${partnerId} not found for this user"))
        }
        val partner = service.get(partnerId)

        if (partnerDto.name != null){
            val foundPartner = service.findByName(partnerDto.name)
            if (foundPartner != null && foundPartner.id != partner.id) {
                throw PartnerDuplicateNameException()
            }
        }

        partner.update(partnerDto.name, partnerDto.description)
        service.save(partner)
        return partner.id
    }
}

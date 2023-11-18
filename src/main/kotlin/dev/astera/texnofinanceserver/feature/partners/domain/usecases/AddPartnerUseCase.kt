package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.core.config.api.CreateResponseDto
import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.feature.partners.domain.errors.PartnerDuplicateNameException
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.partners.presentation.dto.PartnerDto
import dev.astera.texnofinanceserver.feature.users.domain.errors.UserNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import org.springframework.stereotype.Service

interface AddPartnerUseCase {
    suspend operator fun invoke(partnerDto: PartnerDto, sessionUser: SessionUser): CreateResponseDto
}

@Service
internal class AddPartnerUseCaseImpl(
        private val partnerService: PartnerService,
        private val userAggregateService: UserAggregateService,
        private val userService: UserAggregateService
) : AddPartnerUseCase {
    override suspend fun invoke(partnerDto: PartnerDto, sessionUser: SessionUser): CreateResponseDto {
        val foundPartner = partnerService.findByName(partnerDto.name)
        if (foundPartner != null) {
            throw PartnerDuplicateNameException()
        }
        val partner = Partner(name = partnerDto.name, description = partnerDto.description)
        partnerService.save(partner)

        val user = userAggregateService.getByLogin(sessionUser.login) ?: throw UserNotFoundException()
        user.addPartner(partner.id)
        userService.save(user)
        return CreateResponseDto(partner.id)
    }
}
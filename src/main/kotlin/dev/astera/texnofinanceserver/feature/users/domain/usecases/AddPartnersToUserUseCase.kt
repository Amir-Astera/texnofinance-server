package dev.astera.texnofinanceserver.feature.users.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.errors.PartnerNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import dev.astera.texnofinanceserver.feature.users.presentation.dto.AddPartnersToUserDto
import org.springframework.stereotype.Service

interface AddPartnersToUserUseCase {
    suspend operator fun invoke(userId: String, dto: AddPartnersToUserDto)
}

@Service
internal class AddPartnersToUserUseCaseImpl(
        private val userService: UserAggregateService,
) : AddPartnersToUserUseCase {
    override suspend fun invoke(userId: String, dto: AddPartnersToUserDto) {
        val user = userService.get(userId)

        val partnerIds = dto.partnerIds
        val partners = userService.getPartners(partnerIds)
        if (partners.size != partnerIds.size) {
            throw PartnerNotFoundException()
        }
        partners.forEach { partner ->
            user.addPartner(partner.id)
        }
        return userService.save(user)
    }

}
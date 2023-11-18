package dev.astera.texnofinanceserver.feature.users.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.errors.PartnerNotFoundException
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import dev.astera.texnofinanceserver.feature.users.presentation.dto.DeletePartnersFromUserDto
import org.springframework.stereotype.Service

interface DeletePartnersFromUserUseCase {
    suspend operator fun invoke(userId: String, dto: DeletePartnersFromUserDto)
}

@Service
internal class DeletePartnersFromUserUseCaseImpl(
        private val userService: UserAggregateService,
) : DeletePartnersFromUserUseCase {
    override suspend fun invoke(userId: String, dto: DeletePartnersFromUserDto){
            val user = userService.get(userId)
            val partnerIds = dto.partnerIds
            val partners = userService.getPartners(partnerIds)
            if (partners.size != partnerIds.size) {
                throw PartnerNotFoundException()
            }
            partners.forEach { partner ->
                user.deletePartner(partner.id)
            }
            return userService.save(user)
    }
}
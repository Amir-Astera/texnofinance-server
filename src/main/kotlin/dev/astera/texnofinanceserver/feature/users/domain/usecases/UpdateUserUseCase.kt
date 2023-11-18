package dev.astera.texnofinanceserver.feature.users.domain.usecases

import dev.astera.texnofinanceserver.feature.authority.domain.errors.AuthorityNotFoundException
import dev.astera.texnofinanceserver.feature.authority.domain.services.AuthorityAggregateService
import dev.astera.texnofinanceserver.feature.partners.domain.errors.PartnerNotFoundException
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.users.domain.errors.UserDuplicateLoginException
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import dev.astera.texnofinanceserver.feature.users.presentation.dto.UpdateUserDto
import org.springframework.stereotype.Service

interface UpdateUserUseCase {
    suspend operator fun invoke(id: String, dto: UpdateUserDto): String
}

@Service
internal class UpdateUserUseCaseImpl(
        private val userService: UserAggregateService,
        private val authorityService: AuthorityAggregateService,
        private val partnerService: PartnerService
) : UpdateUserUseCase {
    override suspend fun invoke(id: String, dto: UpdateUserDto): String {

        if (userService.existsWithPhone(dto.phone) || userService.existsWithEmail(dto.email)) {
            throw UserDuplicateLoginException()
        }

        val user = userService.get(id)

        user.update(dto)

        val authorityIds = dto.authorityIds?.map { it } ?: emptyList()
        if (authorityIds.isNotEmpty()) {
            val authorities = authorityService.getAllByIds(authorityIds)
            if (authorities.size != authorityIds.size) {
                throw AuthorityNotFoundException()
            }
            user.updateAuthorities(authorityIds)
        }
        val partnerIds = dto.partnerIds?.map { it } ?: emptyList()
        if (partnerIds.isNotEmpty()) {
            val partners = partnerService.getAllByIds(partnerIds)
            if (partners.size != partnerIds.size) {
                throw PartnerNotFoundException()
            }
            user.updatePartners(partnerIds)
        }
        userService.save(user)
        return user.id
    }
}
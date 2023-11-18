package dev.astera.texnofinanceserver.feature.users.domain.usecases

import dev.astera.texnofinanceserver.core.config.FirebaseConfig
import dev.astera.texnofinanceserver.core.config.api.CreateResponseDto
import dev.astera.texnofinanceserver.feature.authority.domain.errors.AuthorityNotFoundException
import dev.astera.texnofinanceserver.feature.authority.domain.services.AuthorityAggregateService
import dev.astera.texnofinanceserver.feature.partners.domain.errors.PartnerNotFoundException
import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import dev.astera.texnofinanceserver.feature.users.domain.errors.UserDuplicateLoginException
import dev.astera.texnofinanceserver.feature.users.domain.models.UserAggregate
import dev.astera.texnofinanceserver.feature.users.domain.services.UserAggregateService
import dev.astera.texnofinanceserver.feature.users.presentation.dto.CreateUserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface AddUserUseCase {
    suspend operator fun invoke(dto: CreateUserDto): CreateResponseDto
}

@Service
internal class AddUserUseCaseImpl(
        private val userService: UserAggregateService,
        private val authorityService: AuthorityAggregateService,
        private val partnerService: PartnerService,
        @Autowired
        private val firebaseConfig: FirebaseConfig
) : AddUserUseCase {
    override suspend fun invoke(dto: CreateUserDto): CreateResponseDto {

        if (userService.existsWithLogin(dto.login) || userService.existsWithPhone(dto.phone) || userService.existsWithEmail(dto.email)) {
            throw UserDuplicateLoginException()
        }
        val user = UserAggregate(
            name = dto.name,
            login = dto.login ?: (dto.email ?: dto.phone ?: ""),
            surname = dto.surname,
            email = dto.email,
            phone = dto.phone
        )
        val authorityIds = dto.authorityIds?.map { it } ?: emptyList()
        if (authorityIds.isNotEmpty()) {
            val authorities = authorityService.getAllByIds(authorityIds)
            if (authorities.size != authorityIds.size) {
                throw AuthorityNotFoundException()
            }
            authorityIds.forEach { user.addAuthority(it) }
        }
        val partnerIds = dto.partnerIds?.map { it } ?: emptyList()
        if (partnerIds.isNotEmpty()) {
            val partners = partnerService.getAllByIds(partnerIds)
            if (partners.size != partnerIds.size) {
                throw PartnerNotFoundException()
            }
            partnerIds.forEach { user.addPartner(it) }
        }
        userService.save(user)
        return CreateResponseDto(user.id)
    }
}
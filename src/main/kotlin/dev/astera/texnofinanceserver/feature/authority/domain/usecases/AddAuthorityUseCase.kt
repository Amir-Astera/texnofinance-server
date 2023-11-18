package dev.astera.texnofinanceserver.feature.authority.domain.usecases

import dev.astera.texnofinanceserver.core.config.FirebaseConfig
import dev.astera.texnofinanceserver.core.config.api.CreateResponseDto
import dev.astera.texnofinanceserver.feature.authority.domain.errors.AuthorityDuplicateNameException
import dev.astera.texnofinanceserver.feature.authority.domain.models.Authority
import dev.astera.texnofinanceserver.feature.authority.domain.services.AuthorityAggregateService
import dev.astera.texnofinanceserver.feature.authority.presentation.dto.CreateAuthorityDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface AddAuthorityUseCase {
    suspend operator fun invoke(dto: CreateAuthorityDto): CreateResponseDto
}

@Service
internal class AddAuthorityUseCaseImpl(
        private val authorityService: AuthorityAggregateService,
//        @Autowired
//        private val firebaseConfig: FirebaseConfig
) : AddAuthorityUseCase {
    override suspend fun invoke(dto: CreateAuthorityDto): CreateResponseDto {
        val foundAuthority = authorityService.findByName(dto.name)
        if (foundAuthority != null) {
            throw AuthorityDuplicateNameException()
        }
        val authority = Authority(
            name = dto.name,
            description = dto.description,
            updatedAt = LocalDateTime.now()
        )
        authorityService.save(authority)
        return CreateResponseDto(authority.id)
    }
}

package dev.astera.texnofinanceserver.feature.authority.presentation.rest

import dev.astera.texnofinanceserver.core.config.api.Controller
import dev.astera.texnofinanceserver.core.config.api.CreateApiResponses
import dev.astera.texnofinanceserver.core.config.api.CreateResponseDto
import dev.astera.texnofinanceserver.core.config.api.OkApiResponses
import dev.astera.texnofinanceserver.feature.authority.domain.models.Authority
import dev.astera.texnofinanceserver.feature.authority.domain.usecases.AddAuthorityUseCase
import dev.astera.texnofinanceserver.feature.authority.domain.usecases.DeleteAuthorityUseCase
import dev.astera.texnofinanceserver.feature.authority.domain.usecases.GetAllAuthorityInfoUseCase
import dev.astera.texnofinanceserver.feature.authority.domain.usecases.UpdateAuthorityUseCase
import dev.astera.texnofinanceserver.feature.authority.presentation.dto.CreateAuthorityDto
import dev.astera.texnofinanceserver.feature.authority.presentation.dto.UpdateAuthorityDto
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/authorities")
@Tag(name = "authorities", description = "The Authorities API")
@SecurityRequirement(name = "security_auth")
class AuthorityController(
        logger: Logger,
        private val addAuthorityUseCase: AddAuthorityUseCase,
        private val updateAuthorityUseCase: UpdateAuthorityUseCase,
        private val getAllAuthorityInfoUseCase: GetAllAuthorityInfoUseCase,
        private val deleteAuthorityUseCase: DeleteAuthorityUseCase,
) : Controller(logger) {

    @CreateApiResponses
    @PostMapping
    suspend fun create(
            @RequestBody createAuthority: CreateAuthorityDto,
            @Parameter(hidden = true) request: ServerHttpRequest
    ): ResponseEntity<CreateResponseDto> {
        try {
            val response = addAuthorityUseCase(createAuthority)
            return HttpStatus.CREATED.response(response, "${request.uri}/${response.id}")
        } catch (ex: Exception) {
            val (code: HttpStatus, message: String?) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @GetMapping
    suspend fun getAll(): ResponseEntity<Collection<Authority>> {
        return try {
            HttpStatus.OK.response(getAllAuthorityInfoUseCase())
        } catch (ex: Exception){
            val (code,message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @OkApiResponses
    @PutMapping("/{id}")
    suspend fun updateAuthority(
        @PathVariable id: String,
        @RequestBody updateAuthorityDto: UpdateAuthorityDto
    ): ResponseEntity<Void> {
        try {
            updateAuthorityUseCase(id, updateAuthorityDto)
            return HttpStatus.OK.response()
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @OkApiResponses
    @DeleteMapping("{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<Void> {
        try {
            deleteAuthorityUseCase(id)
            return HttpStatus.OK.response()
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

}

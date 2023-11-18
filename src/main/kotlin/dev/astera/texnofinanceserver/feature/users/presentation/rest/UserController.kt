package dev.astera.texnofinanceserver.feature.users.presentation.rest

import dev.astera.texnofinanceserver.core.config.api.Controller
import dev.astera.texnofinanceserver.core.config.api.CreateApiResponses
import dev.astera.texnofinanceserver.core.config.api.CreateResponseDto
import dev.astera.texnofinanceserver.core.config.api.OkApiResponses
import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.core.security.firebase.FirebaseSecurityUtils
import dev.astera.texnofinanceserver.feature.users.domain.models.UserAggregate
import dev.astera.texnofinanceserver.feature.users.domain.usecases.*
import dev.astera.texnofinanceserver.feature.users.presentation.dto.*
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.reactive.awaitFirst
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/api/users")
@Tag(name = "users", description = "The Users API")
class UserController(
        logger: Logger,
        private val addUserUseCase: AddUserUseCase,
        private val getUserUseCase: GetUserUseCase,
        private val addAuthoritiesToUserUseCase: AddAuthoritiesToUserUseCase,
        private val addPartnersToUserUseCase: AddPartnersToUserUseCase,
        private val deleteAuthoritiesFromUserUseCase: DeleteAuthoritiesFromUserUseCase,
        private val deletePartnersFromUserUseCase: DeletePartnersFromUserUseCase,
        private val getUserBySessionUseCase: GetUserBySessionUseCase,
        private val updateUserUseCase: UpdateUserUseCase,
        private val deleteUserUseCase: DeleteUserUseCase
): Controller(logger) {

    @SecurityRequirement(name = "security_auth")
    @CreateApiResponses
    @PostMapping
    suspend fun create(
            @RequestBody createUser: CreateUserDto,
            @Parameter(hidden = true) request: ServerHttpRequest
    ): ResponseEntity<CreateResponseDto> {
        try {
            val response = addUserUseCase(createUser)
            return HttpStatus.CREATED.response(response, "${request.uri}/${response.id}")
        } catch (ex: Exception) {
            val (code: HttpStatus, message: String?) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

//    @SecurityRequirement(name = "security_auth")
    @ApiResponses(
            ApiResponse(responseCode = "200", description = "ok",
                    content = [Content(schema = Schema(implementation = UserAggregate::class))])
    )
    @GetMapping("/{userId}")
    suspend fun get(@PathVariable userId: String): ResponseEntity<UserAggregate> {
        try {
            return HttpStatus.OK.response(getUserUseCase(userId))
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @CreateApiResponses
    @PostMapping("/{userId}/authorities")
    suspend fun addAuthorities(
            @PathVariable userId: String,
            @RequestBody dto: AddAuthoritiesToUserDto
    ): ResponseEntity<CreateResponseDto> {
        try {
            addAuthoritiesToUserUseCase(userId, dto)
            return HttpStatus.OK.response()
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @CreateApiResponses
    @PostMapping("/{userId}/partners")
    suspend fun addPartners(
            @PathVariable userId: String,
            @RequestBody dto: AddPartnersToUserDto
    ): ResponseEntity<CreateResponseDto> {
        try {
            addPartnersToUserUseCase(userId, dto)
            return HttpStatus.OK.response()
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @OkApiResponses
    @PutMapping("{id}")
    suspend fun update(
            @PathVariable id: String,
            @RequestBody dto: UpdateUserDto
    ): ResponseEntity<Void> {
        try {
            updateUserUseCase(id, dto)
            return HttpStatus.OK.response()
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @OkApiResponses
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<Void> {
        try {
            deleteUserUseCase(id)
            return HttpStatus.OK.response()
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @OkApiResponses
    @DeleteMapping("/{userId}/authorities")
    suspend fun deleteAuthorities(
            @PathVariable userId: String,
            @RequestBody dto: DeleteAuthoritiesFromUserDto
    ): ResponseEntity<Void> {
        try {
            deleteAuthoritiesFromUserUseCase(userId, dto)
            return HttpStatus.OK.response()
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @OkApiResponses
    @DeleteMapping("/{userId}/partners")
    suspend fun deletePartners(
            @PathVariable userId: String,
            @RequestBody dto: DeletePartnersFromUserDto
    ): ResponseEntity<Void> {
        try {
            deletePartnersFromUserUseCase(userId, dto)
            return HttpStatus.OK.response()
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @GetMapping("/current")
    suspend fun getSession(
            @Parameter(hidden = true) exchange: ServerWebExchange
    ): ResponseEntity<UserAggregate> {
        val user = getSessionUser(exchange)
        try {
            return HttpStatus.OK.response(getUserBySessionUseCase(user))
        } catch (ex: Exception) {
            val (code, message) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    private suspend fun getSessionUser(exchange: ServerWebExchange): SessionUser {
        return FirebaseSecurityUtils.getUserFromRequest(exchange).awaitFirst()
    }
}
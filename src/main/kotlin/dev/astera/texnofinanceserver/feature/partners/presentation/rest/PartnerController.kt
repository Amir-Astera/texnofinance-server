package dev.astera.texnofinanceserver.feature.partners.presentation.rest

import dev.astera.texnofinanceserver.core.config.api.*
import dev.astera.texnofinanceserver.core.security.SessionUser
import dev.astera.texnofinanceserver.core.security.firebase.FirebaseSecurityUtils.getUserFromRequest
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.partners.domain.usecases.*
import dev.astera.texnofinanceserver.feature.partners.presentation.dto.GetAllPartnerResponse
import dev.astera.texnofinanceserver.feature.partners.presentation.dto.PartnerDto
import dev.astera.texnofinanceserver.feature.partners.presentation.dto.UpdatePartnerDto
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.Logger
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/partners")
@Tag(name = "partners", description = "The Partners API")
class PartnerController(
		logger: Logger,
		private val addPartnerUseCase: AddPartnerUseCase,
		private val getPartnerInfoUseCase: GetPartnerUseCase,
		private val getAllPartnerInfoUseCase: GetAllPartnerInfoUseCase,
		private val updatePartnerUseCase: UpdatePartnerUseCase,
		private val statusActivateUseCase: StatusActivateUseCase,
		private val statusDeactivateUseCase: StatusDeactivateUseCase,
		private val statusArchiveUseCase: StatusArchiveUseCase,
		private val addCostUseCase: AddCostUseCase,
		private val addPartnerCostUseCase: AddPartnerCostUseCase
) : Controller(logger) {

	@SecurityRequirement(name = "security_auth")
	@CreateApiResponses
	@PostMapping
	suspend fun create(
			@RequestBody partnerDto: PartnerDto,
			@Parameter(hidden = true) request: ServerHttpRequest,
			@Parameter(hidden = true) exchange: ServerWebExchange
	): ResponseEntity<CreateResponseDto> {
		val user = getSessionUser(exchange)
		try {
			val response = addPartnerUseCase(partnerDto, user)
			return HttpStatus.CREATED.response(response, "${request.uri}/${response.id}")
		} catch (ex: Exception) {
			val (code, message) = getError(ex)
			throw ResponseStatusException(code, message)
		}
	}

//	@SecurityRequirement(name = "security_auth")
//	@PutMapping("/{id}/cost/{count}/month/{}")
//	@ApiResponses(
//		ApiResponse(
//			responseCode = "200", description = "ok",
//			content = [Content(schema = Schema(implementation = Unit::class))]
//		)
//	)
//	suspend fun addCost(
//		@PathVariable id: String,
//		@PathVariable count: Double,
//		@RequestParam
//		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
//		month: LocalDateTime,
//		@Parameter(hidden = true) exchange: ServerWebExchange
//	): ResponseEntity<Unit> {
//		try {
//		    addCostUseCase(count, month)
//			return HttpStatus.OK.response()
//		} catch (ex: Exception) {
//			val (code, message) = getError(ex)
//			throw ResponseStatusException(code, message)
//		}
//	}

	@SecurityRequirement(name = "security_auth")
	@PutMapping("/{id}/cost")
	@ApiResponses(
		ApiResponse(
			responseCode = "200", description = "ok",
			content = [Content(schema = Schema(implementation = Unit::class))]
		)
	)
	suspend fun addPartnerCost(
		@PathVariable id: String,
		@RequestParam(required = false)
		count: Double? = null,
		@RequestParam(required = false)
		stableCost: Double? = null,
		@RequestParam
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
		day: LocalDateTime,
		@Parameter(hidden = true) exchange: ServerWebExchange
	): ResponseEntity<Unit> {
		try {
			addPartnerCostUseCase(id, count, stableCost, day)
			return HttpStatus.OK.response()
		} catch (ex: Exception) {
			val (code, message) = getError(ex)
			throw ResponseStatusException(code, message)
		}
	}
	@SecurityRequirement(name = "security_auth")
	@GetApiResponses
	@ApiResponses(
			ApiResponse(
					responseCode = "200", description = "ok",
					content = [Content(schema = Schema(implementation = Partner::class))]
			)
	)
	@GetMapping("/{id}")
	suspend fun get(
			@PathVariable id: String,
			@Parameter(hidden = true) exchange: ServerWebExchange
	): ResponseEntity<Partner> {
		val request = exchange.request
		val user = getSessionUser(exchange)
		try {
			val response = getPartnerInfoUseCase.invoke(id, user)
			return HttpStatus.OK.response(response, "${request.uri}")
		} catch (ex: Exception) {
			val (code, message) = getError(ex)
			throw ResponseStatusException(code, message)
		}
	}

	@SecurityRequirement(name = "security_auth")
	@GetApiResponses
	@ApiResponses(
			ApiResponse(responseCode = "200", description = "ok",
					content = [Content(array = ArraySchema(schema = Schema(implementation = Partner::class)))])
	)
	@GetMapping
	suspend fun getAll(
			@Parameter(hidden = true) exchange: ServerWebExchange
	): ResponseEntity<Collection<GetAllPartnerResponse>> {
		val request = exchange.request
		val user = getSessionUser(exchange)
		try {
			val response = getAllPartnerInfoUseCase.invoke(user)
			return HttpStatus.OK.response(response, "${request.uri}")
		} catch (ex: Exception) {
			val (code, message) = getError(ex)
			throw ResponseStatusException(code, message)
		}
	}

	@SecurityRequirement(name = "security_auth")
	@OkApiResponses
	@PutMapping("/{id}")
	suspend fun updateName(
			@PathVariable id: String, @RequestBody partnerDto: UpdatePartnerDto,
			@Parameter(hidden = true) exchange: ServerWebExchange
	): ResponseEntity<Void> {
		val user = getSessionUser(exchange)
		try {
			updatePartnerUseCase.invoke(id, partnerDto, user)
			return HttpStatus.OK.response()
		} catch (ex: Exception) {
			val (code, message) = getError(ex)
			throw ResponseStatusException(code, message)
		}
	}

	@SecurityRequirement(name = "security_auth")
	@OkApiResponses
	@PutMapping("{id}/deactivate")
	suspend fun changeStatusToInactive(@PathVariable id: String): ResponseEntity<Void> {
		try {
			statusDeactivateUseCase(id)
			return HttpStatus.OK.response()
		} catch (ex: Exception) {
			val (code, message) = getError(ex)
			throw ResponseStatusException(code, message)
		}
	}

	@SecurityRequirement(name = "security_auth")
	@OkApiResponses
	@PutMapping("{id}/activate")
	suspend fun changeStatusToActive(@PathVariable id: String): ResponseEntity<Void> {
		try {
			statusActivateUseCase(id)
			return HttpStatus.OK.response()
		} catch (ex: Exception) {
			val (code, message) = getError(ex)
			throw ResponseStatusException(code, message)
		}
	}

	@SecurityRequirement(name = "security_auth")
	@OkApiResponses
	@PutMapping("{id}/archive")
	suspend fun changeStatusToArchived(@PathVariable id: String): ResponseEntity<Void> {
		try {
			statusArchiveUseCase(id)
			return HttpStatus.OK.response()
		} catch (ex: Exception) {
			val (code, message) = getError(ex)
			throw ResponseStatusException(code, message)
		}
	}


//	@SecurityRequirement(name = "security_auth")
//	@CreateApiResponses
//	@ApiResponses(
//			ApiResponse(responseCode = "504", description = "gateway error",
//					content = [Content(schema = Schema(implementation = Void::class))])
//	)
//	@PutMapping("{partnerId}/logo", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
//	suspend fun uploadLogo(
//			@PathVariable partnerId: String,
//			@RequestPart file: FilePart
//	): ResponseEntity<Void> {
//		try {
//			val allTypes = MimeTypes.getDefaultMimeTypes()
//			val fileType = allTypes.forName(file.headers().contentType.toString())
//
//			if (!properties.allowedTypes.contains(fileType.toString())) {
//				throw LogoNotAllowedException("Content not allowed: $fileType")
//			}
//			updateLogoToPartnerUseCase(partnerId, file, fileType.extension)
//			return HttpStatus.OK.response()
//		} catch (ex: Exception) {
//			val (code, message) = getError(ex)
//			throw ResponseStatusException(code, message)
//		}
//	}
//
//	@GetMapping("{partnerId}/logo.{ext}",
//			produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
//	suspend fun getFile(
//			@PathVariable partnerId: String,
//			@PathVariable ext: String
//	): Resource {
//		return FileSystemResource("files/$partnerId/logo.$ext")
//	}
//
	private suspend fun getSessionUser(exchange: ServerWebExchange): SessionUser {
		return getUserFromRequest(exchange).awaitSingle()
	}
}

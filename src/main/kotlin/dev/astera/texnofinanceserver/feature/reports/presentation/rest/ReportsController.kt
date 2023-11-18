package dev.astera.texnofinanceserver.feature.reports.presentation.rest

import dev.astera.texnofinanceserver.core.config.api.Controller
import dev.astera.texnofinanceserver.core.config.api.CreateApiResponses
import dev.astera.texnofinanceserver.core.config.api.CreateResponseDto
import dev.astera.texnofinanceserver.feature.authority.presentation.dto.CreateAuthorityDto
import dev.astera.texnofinanceserver.feature.reports.domain.models.FinalReport
import dev.astera.texnofinanceserver.feature.reports.domain.models.Graph
import dev.astera.texnofinanceserver.feature.reports.domain.usecases.AddReportUseCase
import dev.astera.texnofinanceserver.feature.reports.domain.usecases.GetReportGraphUseCase
import dev.astera.texnofinanceserver.feature.reports.domain.usecases.GetReportsUseCase
import dev.astera.texnofinanceserver.feature.reports.presentation.dto.CreateReportDto
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.Month

@RestController
@RequestMapping("/api/reports")
@Tag(name = "reports", description = "The Reports API")
class ReportsController(
        logger: Logger,
        private val addReportUseCase: AddReportUseCase,
        private val getReportUseCase: GetReportsUseCase,
        private val getReportGraphUseCase: GetReportGraphUseCase
): Controller(logger) {

    @SecurityRequirement(name = "security_auth")
    @CreateApiResponses
    @PostMapping("/{partnerId}")
    suspend fun create(
            @PathVariable partnerId: String,
            @RequestBody createReportDto: CreateReportDto,
            @Parameter(hidden = true) request: ServerHttpRequest
    ): ResponseEntity<CreateResponseDto> {
        try {
            val response = addReportUseCase(partnerId, createReportDto)
            return HttpStatus.CREATED.response(response, "${request.uri}/${response.id}")
        } catch (ex: Exception) {
            val (code: HttpStatus, message: String?) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @CreateApiResponses
    @GetMapping("/{partnerId}")
    suspend fun get(
        @PathVariable partnerId: String,
        @Parameter(hidden = true) request: ServerHttpRequest,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        fromDate: LocalDateTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        toDate: LocalDateTime,
    ): ResponseEntity<FinalReport> {
        try {
            val response = getReportUseCase(partnerId, fromDate, toDate)
            return HttpStatus.OK.response(response)
        } catch (ex: Exception) {
            val (code: HttpStatus, message: String?) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }

    @SecurityRequirement(name = "security_auth")
    @CreateApiResponses
    @GetMapping("/graph/{partnerId}")
    suspend fun getGraph(
        @PathVariable partnerId: String,
    ): ResponseEntity<List<Graph>> {
        try {
            val response = getReportGraphUseCase(partnerId)
            return HttpStatus.OK.response(response)
        } catch (ex: Exception) {
            val (code: HttpStatus, message: String?) = getError(ex)
            throw ResponseStatusException(code, message)
        }
    }
}
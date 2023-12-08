package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface AddPartnerCostUseCase {
    suspend operator fun invoke(partnerId: String, count: Double? = null, stableCost: Double? = null, day: LocalDateTime): Unit
}

@Service
class AddPartnerCostUseCaseImpl(
    private val partnerService: PartnerService
): AddPartnerCostUseCase {
    override suspend operator fun invoke(partnerId: String, count: Double?, stableCost: Double?, day: LocalDateTime) {
        partnerService.addPartnerCost(partnerId, count, stableCost, day)
    }
}
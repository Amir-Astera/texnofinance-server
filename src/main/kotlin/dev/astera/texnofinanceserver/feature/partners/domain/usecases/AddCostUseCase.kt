package dev.astera.texnofinanceserver.feature.partners.domain.usecases

import dev.astera.texnofinanceserver.feature.partners.domain.services.PartnerService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface AddCostUseCase {
    suspend operator fun invoke(count: Double, month: LocalDateTime): Unit
}
@Service
class AddCostUseCaseImpl(
    private val partnerService: PartnerService
): AddCostUseCase {
    override suspend operator fun invoke(count: Double, month: LocalDateTime) {
        return partnerService.addCost(count, month)
    }

}
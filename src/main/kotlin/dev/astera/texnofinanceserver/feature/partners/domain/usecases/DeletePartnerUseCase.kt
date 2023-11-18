//package dev.bytepride.billboardserver.domain.application.usecase
//
//import dev.bytepride.billboardserver.domain.application.service.PartnerAggregateService
//import dev.bytepride.billboardserver.domain.core.model.PartnerAggregate
//import org.springframework.stereotype.Service
//
//interface DeletePartnerUseCase {
//	suspend operator fun invoke(id: String): PartnerAggregate
//}
//
//@Service
//internal class DeletePartnerUseCaseImpl(
//	private val service: PartnerAggregateService
//) : DeletePartnerUseCase {
//	override suspend fun invoke(id: String): PartnerAggregate{
//		val aggregate = service.get(id)
//		aggregate.delete(id)
//		service.save(aggregate)
//		return aggregate
//	}
//}
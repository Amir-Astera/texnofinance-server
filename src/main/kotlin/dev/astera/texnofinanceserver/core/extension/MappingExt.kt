package dev.astera.texnofinanceserver.core.extension

import dev.astera.texnofinanceserver.feature.authority.data.entity.AuthorityEntity
import dev.astera.texnofinanceserver.feature.authority.domain.models.Authority
import dev.astera.texnofinanceserver.feature.partners.data.entity.PartnerEntity
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner
import dev.astera.texnofinanceserver.feature.partners.domain.models.Partner.PartnerStatus
import dev.astera.texnofinanceserver.feature.reports.data.entity.PartnerReportEntity
import dev.astera.texnofinanceserver.feature.reports.data.entity.ReportEntity
import dev.astera.texnofinanceserver.feature.reports.domain.models.ReportAggregate
import dev.astera.texnofinanceserver.feature.users.data.entity.UserEntity
import dev.astera.texnofinanceserver.feature.users.domain.models.UserAggregate
import java.time.LocalDateTime

fun UserEntity.toModel(
        authorities: Collection<Authority>,
        partners: Collection<Partner>
): UserAggregate {
    return UserAggregate(
            id = id,
            name = name,
            surname = surname,
            email = email,
            phone = phone,
            login = login,
            authorities = authorities,
            partners = partners,
            logoUrl = logo,
            version = version,
            createdAt = createdAt,
            updatedAt = updatedAt
    )
}

fun AuthorityEntity.toModel(
        version: Long? = null,
        createdAt: LocalDateTime? = null
): Authority {
    return Authority(
            id = id,
            name = name,
            description = description,
            version = version ?: this.version,
            createdAt = createdAt ?: this.createdAt,
            updatedAt = updatedAt
    )
}

fun Authority.toEntity(): AuthorityEntity {
    return AuthorityEntity(
            id = id,
            name = name,
            description = description,
            version = version,
            createdAt = createdAt,
            updatedAt = updatedAt
    )
}

fun PartnerEntity.toModel(
        version: Long? = null,
        createdAt: LocalDateTime? = null,
        reports: Collection<ReportAggregate>? = null
): Partner {

    return Partner(
            id = id,
            reports = reports ?: emptyList(),
            name = name,
            description = description,
            status = PartnerStatus.from(status),
            version = version ?: this.version,
            createdAt = createdAt ?: this.createdAt,
            updatedAt = updatedAt,
            logoUrl = logo
    )
}

fun Partner.toEntity(): PartnerEntity {

    return PartnerEntity(
            id = id,
            name = name,
            description = description,
            version = version,
            createdAt = createdAt,
            status = status.value,
            logo = logoUrl
    )
}

fun UserAggregate.toEntity(): UserEntity {
    return UserEntity(
            id = id,
            name = name,
            surname = surname,
            email = email,
            phone = phone,
            login = login,
            logo = logoUrl,
            version = version,
            createdAt = createdAt,
            updatedAt = updatedAt
    )
}

fun ReportEntity.toModel(): ReportAggregate {
    return ReportAggregate(
            id = id,
            reportDate = reportDate,
            profit = profit,
            profitStore = profitStore,
            otherProfit = otherProfit,
            clients = clients,
            newClients = newClients,
            netIssue = netIssue,
            cost = cost,
            invoice = invoice,
            auctioneerEquipment = auctioneerEquipment,
            auctioneerCoat = auctioneerCoat,
            cashInDeposit = cashInDeposit,
            note = note,
            version = version,
            auctioneerGold = auctioneerGold,
            cashInDepositGold = cashInDepositGold,
            createdAt = createdAt ?: LocalDateTime.now()
    )
}

fun ReportAggregate.toEntity(): ReportEntity {
    return ReportEntity(
            id = id,
            reportDate = reportDate,
            profit = profit,
            profitStore = profitStore,
            otherProfit = otherProfit,
            clients = clients,
            newClients = newClients,
            netIssue = netIssue,
            cost = cost,
            invoice = invoice,
            auctioneerEquipment = auctioneerEquipment,
            auctioneerCoat = auctioneerCoat,
            cashInDeposit = cashInDeposit,
            note = note,
            version = version,
            auctioneerGold = auctioneerGold,
            cashInDepositGold = cashInDepositGold,
            createdAt = createdAt ?: LocalDateTime.now()
    )
}
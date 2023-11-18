package dev.astera.texnofinanceserver.feature.partners.domain.models

import dev.astera.texnofinanceserver.feature.authority.domain.models.Authority
import dev.astera.texnofinanceserver.feature.reports.domain.models.ReportAggregate
import java.time.LocalDateTime
import java.util.*

class Partner(
        val id: String = UUID.randomUUID().toString(),
        var name: String = "",
        var description: String? = null,
        val createdAt: LocalDateTime? = null,
        val version: Long? = null,
        var status: PartnerStatus = PartnerStatus.ACTIVE,
        var logoUrl: String? = null,
        var updatedAt: LocalDateTime = LocalDateTime.now(),
        reports: Collection<ReportAggregate> = emptyList(),
) {
    fun updateStatus(status: PartnerStatus) {
        this.status = status
        this.updatedAt = LocalDateTime.now()
    }

    fun update(name: String?, description: String?) {
        if (description != null) {
            this.description = description
        }
        if (name != null) {
            this.name = name
        }
        this.updatedAt = LocalDateTime.now()
    }

    private val _reports = reports.toMutableList()
    val reports: Collection<ReportAggregate>
        get() = _reports


    fun addReport(report: ReportAggregate): Partner {
        _reports.find { it.id == report.id }
                ?: report.also { _reports.add(it) }
        return this
    }

    enum class PartnerStatus(val value: Short) {
        ACTIVE(1),
        INACTIVE(2),
        ARCHIVED(3);

        companion object {
            fun from(value: Short): PartnerStatus {
                return values().find { it.value == value }
                        ?: throw IllegalArgumentException("Status '$value' not defined")
            }
        }
    }
}

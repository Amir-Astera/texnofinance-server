package dev.astera.texnofinanceserver.feature.reports.domain.models

import java.time.Month

data class Graph(
    val name: Month,
    val value: Double
)

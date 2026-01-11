package com.arekalov.osrubezh.presentation.numa

import com.arekalov.osrubezh.domain.model.NUMAResult

data class NUMAUiState(
    val frequencyGHz: String = "",
    val l1l2TimeNs: String = "",
    val localNumaTimeNs: String = "",
    val otherNumaTimeNs: String = "",
    val registersCount: String = "",
    val l1l2Count: String = "",
    val localNumaCount: String = "",
    val otherNumaCount: String = "",
    val result: NUMAResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDetails: Boolean = false
)

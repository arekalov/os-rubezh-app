package com.arekalov.osrubezh.presentation.raid

import com.arekalov.osrubezh.domain.model.RAIDResult

data class RAIDUiState(
    val raidLevel: String = "",
    val diskCount: String = "",
    val diskSizeGB: String = "",
    val blockSize: String = "",
    val suSize: String = "",
    val blockRead: String = "",
    val blockWrite: String = "",
    val suCalc: String = "",
    val result: RAIDResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDetails: Boolean = false
)

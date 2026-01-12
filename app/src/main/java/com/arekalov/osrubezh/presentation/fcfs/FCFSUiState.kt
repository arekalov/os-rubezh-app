package com.arekalov.osrubezh.presentation.fcfs

import com.arekalov.osrubezh.domain.model.FCFSResult

data class FCFSUiState(
    val cpuTime: String = "",
    val ioTime: String = "",
    val totalWorkTime: String = "",
    val processCount: String = "",
    val result: FCFSResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDetails: Boolean = false
)

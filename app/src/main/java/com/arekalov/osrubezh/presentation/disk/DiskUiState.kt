package com.arekalov.osrubezh.presentation.disk

import com.arekalov.osrubezh.domain.model.DiskResult

data class DiskUiState(
    val bytePerSector: String = "",
    val sectorsPerTrack: String = "",
    val tracksPerSurface: String = "",
    val surfacesPerDisk: String = "",
    val recordsCount: String = "",
    val bytesPerRecord: String = "",
    val result: DiskResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDetails: Boolean = false
)

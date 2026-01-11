package com.arekalov.osrubezh.presentation.pointers

import com.arekalov.osrubezh.domain.model.PointersResult

data class PointersUiState(
    val directPointers: String = "",
    val pointerSizeBits: String = "",
    val blockSizeBytes: String = "",
    val fileSizeBytes: String = "",
    val result: PointersResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDetails: Boolean = false
)

package com.arekalov.osrubezh.presentation.pointers

import com.arekalov.osrubezh.domain.model.PointersResult

data class PointersUiState(
    val directPointers: String = "16",
    val pointerSizeBits: String = "32",
    val blockSizeBytes: String = "512",
    val fileSizeBytes: String = "235477247",
    val result: PointersResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDetails: Boolean = false
)

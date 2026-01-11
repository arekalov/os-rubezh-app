package com.arekalov.osrubezh.domain.model

data class PointersResult(
    val totalBlocks: Long,
    val systemBytes: Long,
    val systemBlocks: Long,
    val fileBytes: Long,
    val fileBlocks: Long,
    val totalBytes: Long,
    val directPointers: Int,
    val pointerSizeBits: Int,
    val blockSizeBytes: Int,
    val fileSizeBytes: Long,
    val pointersPerBlock: Int
)

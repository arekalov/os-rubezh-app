package com.arekalov.osrubezh.domain.model

data class RAIDResult(
    val answer: Double,
    val answerMinutes: Double,
    val raidLevel: Int,
    val diskCount: Int,
    val validDisks: Int,
    val diskSizeGB: Int,
    val diskSizeBytes: Long,
    val blockSize: Int,
    val suSize: Int,
    val blockRead: Int,
    val blockWrite: Int,
    val suCalc: Int,
    val stripes: Double,
    val blockCount: Long,
    val writeTime: Double,
    val calculateTime: Double,
    val readTime: Double
)

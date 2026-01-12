package com.arekalov.osrubezh.domain.model

data class ProcessResult(
    val processNumber: Int,
    val cpuTime: Int,
    val waitTime: Int,
    val readyTime: Int,
    val totalTime: Int
)

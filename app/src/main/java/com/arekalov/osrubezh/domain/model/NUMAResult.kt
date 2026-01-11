package com.arekalov.osrubezh.domain.model

data class NUMAResult(
    val answer: Double,
    val frequencyGHz: Double,
    val registerTime: Double,
    val l1l2Time: Double,
    val localNumaTime: Double,
    val otherNumaTime: Double,
    val registersBefore: Int,
    val l1l2Before: Int,
    val localNumaBefore: Int,
    val otherNumaBefore: Int,
    val registersAfter: Int,
    val l1l2After: Int,
    val localNumaAfter: Int,
    val otherNumaAfter: Int,
    val timeBefore: Double,
    val timeAfter: Double
)

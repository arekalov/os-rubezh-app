package com.arekalov.osrubezh.domain.usecase

import com.arekalov.osrubezh.data.calculator.NUMACalculator
import com.arekalov.osrubezh.domain.model.NUMAResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculateNUMAUseCase @Inject constructor(
    private val numaCalculator: NUMACalculator
) {
    suspend operator fun invoke(
        frequencyGHz: Double,
        l1l2TimeNs: Double,
        localNumaTimeNs: Double,
        otherNumaTimeNs: Double,
        registersCount: Int,
        l1l2Count: Int,
        localNumaCount: Int,
        otherNumaCount: Int
    ): Result<NUMAResult> = withContext(Dispatchers.Default) {
        try {
            val result = numaCalculator.calculate(
                frequencyGHz = frequencyGHz,
                l1l2TimeNs = l1l2TimeNs,
                localNumaTimeNs = localNumaTimeNs,
                otherNumaTimeNs = otherNumaTimeNs,
                registersCount = registersCount,
                l1l2Count = l1l2Count,
                localNumaCount = localNumaCount,
                otherNumaCount = otherNumaCount
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

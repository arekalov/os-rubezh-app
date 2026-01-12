package com.arekalov.osrubezh.domain.usecase

import com.arekalov.osrubezh.data.calculator.FCFSCalculator
import com.arekalov.osrubezh.domain.model.FCFSResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculateFCFSUseCase @Inject constructor(
    private val fcfsCalculator: FCFSCalculator
) {
    suspend operator fun invoke(
        cpuTime: Int,
        ioTime: Int,
        totalWorkTime: Int,
        processCount: Int
    ): Result<FCFSResult> = withContext(Dispatchers.Default) {
        try {
            val result = fcfsCalculator.calculate(
                cpuTime = cpuTime,
                ioTime = ioTime,
                totalWorkTime = totalWorkTime,
                processCount = processCount
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

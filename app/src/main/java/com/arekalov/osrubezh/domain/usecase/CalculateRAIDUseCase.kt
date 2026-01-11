package com.arekalov.osrubezh.domain.usecase

import com.arekalov.osrubezh.data.calculator.RAIDCalculator
import com.arekalov.osrubezh.domain.model.RAIDResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculateRAIDUseCase @Inject constructor(
    private val raidCalculator: RAIDCalculator
) {
    suspend operator fun invoke(
        raidLevel: Int,
        diskCount: Int,
        diskSizeGB: Int,
        blockSize: Int,
        suSize: Int,
        blockRead: Int,
        blockWrite: Int,
        suCalc: Int
    ): Result<RAIDResult> = withContext(Dispatchers.Default) {
        try {
            val result = raidCalculator.calculate(
                raidLevel = raidLevel,
                diskCount = diskCount,
                diskSizeGB = diskSizeGB,
                blockSize = blockSize,
                suSize = suSize,
                blockRead = blockRead,
                blockWrite = blockWrite,
                suCalc = suCalc
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

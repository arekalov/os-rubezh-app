package com.arekalov.osrubezh.domain.usecase

import com.arekalov.osrubezh.data.calculator.PointersCalculator
import com.arekalov.osrubezh.domain.model.PointersResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculatePointersUseCase @Inject constructor(
    private val pointersCalculator: PointersCalculator
) {
    suspend operator fun invoke(
        directPointers: Int,
        pointerSizeBits: Int,
        blockSizeBytes: Int,
        fileSizeBytes: Long
    ): Result<PointersResult> = withContext(Dispatchers.Default) {
        try {
            val result = pointersCalculator.calculate(
                directPointers = directPointers,
                pointerSizeBits = pointerSizeBits,
                blockSizeBytes = blockSizeBytes,
                fileSizeBytes = fileSizeBytes
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

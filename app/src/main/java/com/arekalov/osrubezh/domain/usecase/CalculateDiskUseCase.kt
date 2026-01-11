package com.arekalov.osrubezh.domain.usecase

import com.arekalov.osrubezh.data.calculator.DiskCalculator
import com.arekalov.osrubezh.domain.model.DiskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculateDiskUseCase @Inject constructor(
    private val diskCalculator: DiskCalculator
) {
    suspend operator fun invoke(
        bytePerSector: Int,
        sectorsPerTrack: Int,
        tracksPerSurface: Int,
        surfacesPerDisk: Int,
        recordsCount: Int,
        bytesPerRecord: Int
    ): Result<DiskResult> = withContext(Dispatchers.Default) {
        try {
            val result = diskCalculator.calculate(
                bytePerSector = bytePerSector,
                sectorsPerTrack = sectorsPerTrack,
                tracksPerSurface = tracksPerSurface,
                surfacesPerDisk = surfacesPerDisk,
                recordsCount = recordsCount,
                bytesPerRecord = bytesPerRecord
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

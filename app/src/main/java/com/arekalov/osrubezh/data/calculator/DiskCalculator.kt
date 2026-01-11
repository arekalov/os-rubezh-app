package com.arekalov.osrubezh.data.calculator

import com.arekalov.osrubezh.domain.model.DiskResult
import javax.inject.Inject
import kotlin.math.ceil

class DiskCalculator @Inject constructor() {
    
    fun calculate(
        bytePerSector: Int,
        sectorsPerTrack: Int,
        tracksPerSurface: Int,
        surfacesPerDisk: Int,
        recordsCount: Int,
        bytesPerRecord: Int
    ): DiskResult {
        val bytePerTrack = bytePerSector * sectorsPerTrack
        val bytePerSurface = bytePerTrack * tracksPerSurface
        val bytePerDisk = bytePerSurface * surfacesPerDisk
        val fileSize = recordsCount * bytesPerRecord
        
        val recordsInOneSector = bytePerSector / bytesPerRecord
        
        val sectorsForFile = ceil(recordsCount.toDouble() / recordsInOneSector).toInt()
        
        val tracksForFile = ceil(sectorsForFile.toDouble() / sectorsPerTrack).toInt()
        
        val sectorsOnLastTrack = sectorsForFile - (sectorsForFile / sectorsPerTrack) * sectorsPerTrack
        
        val realSizeFile = sectorsForFile * bytePerSector
        
        return DiskResult(
            sectorsForFile = sectorsForFile,
            tracksForFile = tracksForFile,
            realSizeFile = realSizeFile,
            sectorsOnLastTrack = sectorsOnLastTrack,
            bytePerSector = bytePerSector,
            sectorsPerTrack = sectorsPerTrack,
            tracksPerSurface = tracksPerSurface,
            surfacesPerDisk = surfacesPerDisk,
            recordsCount = recordsCount,
            bytesPerRecord = bytesPerRecord,
            recordsInOneSector = recordsInOneSector,
            bytePerTrack = bytePerTrack,
            bytePerSurface = bytePerSurface,
            bytePerDisk = bytePerDisk,
            fileSize = fileSize
        )
    }
}

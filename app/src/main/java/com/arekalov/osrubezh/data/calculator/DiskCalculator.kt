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
        
        // Расчет фрагментации (задание 3)
        // Полезных байт в секторе (с учетом целого числа записей)
        val usefulBytesPerSector = recordsInOneSector * bytesPerRecord
        
        // Фрагментация в одном секторе
        val fragmentationPerSector = bytePerSector - usefulBytesPerSector
        
        // Байт на последнем секторе
        val bytesOnLastSector = fileSize - (sectorsForFile - 1) * usefulBytesPerSector
        
        // Фрагментация на последнем секторе
        val fragmentationOnLastSector = bytePerSector - bytesOnLastSector
        
        // Итоговая фрагментация
        val totalFragmentation = (sectorsForFile - 1) * fragmentationPerSector + fragmentationOnLastSector
        
        return DiskResult(
            sectorsForFile = sectorsForFile,
            tracksForFile = tracksForFile,
            realSizeFile = realSizeFile,
            sectorsOnLastTrack = sectorsOnLastTrack,
            totalFragmentation = totalFragmentation,
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
            fileSize = fileSize,
            usefulBytesPerSector = usefulBytesPerSector,
            fragmentationPerSector = fragmentationPerSector
        )
    }
}

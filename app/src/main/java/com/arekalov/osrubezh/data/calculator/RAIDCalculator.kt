package com.arekalov.osrubezh.data.calculator

import com.arekalov.osrubezh.domain.model.RAIDResult
import javax.inject.Inject

class RAIDCalculator @Inject constructor() {
    
    fun calculate(
        raidLevel: Int,
        diskCount: Int,
        diskSizeGB: Int,
        blockSize: Int,
        suSize: Int,
        blockRead: Int,
        blockWrite: Int,
        suCalc: Int
    ): RAIDResult {
        var validDisks = diskCount - 1
        when (raidLevel) {
            1 -> validDisks = 1
            6 -> validDisks = diskCount - 2
        }
        
        val diskSizeBytes = diskSizeGB.toLong() * 1024L * 1024L * 1024L
        
        val stripes = diskSizeBytes.toDouble() / (blockSize * suSize)
        
        val blockCount = diskSizeBytes / blockSize
        
        val writeTime = blockCount.toDouble() * blockWrite
        
        val calculateTime = stripes * suCalc
        
        val readTime = blockCount.toDouble() * blockRead * validDisks
        
        val answer = writeTime + calculateTime + readTime
        val answerMinutes = answer / 1000.0 / 1000.0 / 60.0
        
        return RAIDResult(
            answer = answer,
            answerMinutes = answerMinutes,
            raidLevel = raidLevel,
            diskCount = diskCount,
            validDisks = validDisks,
            diskSizeGB = diskSizeGB,
            diskSizeBytes = diskSizeBytes,
            blockSize = blockSize,
            suSize = suSize,
            blockRead = blockRead,
            blockWrite = blockWrite,
            suCalc = suCalc,
            stripes = stripes,
            blockCount = blockCount,
            writeTime = writeTime,
            calculateTime = calculateTime,
            readTime = readTime
        )
    }
}

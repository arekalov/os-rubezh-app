package com.arekalov.osrubezh.data.calculator

import com.arekalov.osrubezh.domain.model.PointersResult
import javax.inject.Inject
import kotlin.math.floor

class PointersCalculator @Inject constructor() {
    
    private fun myDiv(a: Int, b: Int): Int {
        return floor((a + b - 1).toDouble() / b).toInt()
    }
    
    fun calculate(
        directPointers: Int,
        pointerSizeBits: Int,
        blockSizeBytes: Int,
        fileSizeBytes: Long
    ): PointersResult {
        val ppp = myDiv(blockSizeBytes * 8, pointerSizeBits)
        val p0 = directPointers
        val l1l2l3 = blockSizeBytes
        val fileBlock = blockSizeBytes
        val l0 = 0
        
        var systemSum = 0L
        var fileSum = 0L
        var remainingSize = fileSizeBytes
        var currentP0 = p0
        
        // Direct pointers (p0)
        while (currentP0 > 0 && remainingSize >= 0) {
            fileSum += fileBlock
            remainingSize -= fileBlock
            currentP0--
        }
        
        // Single indirect (p1)
        var p1_0 = ppp
        val kp1_0 = ppp
        while (p1_0 > 0 && remainingSize >= 0) {
            if (p1_0 == kp1_0) systemSum += l1l2l3
            p1_0--
            fileSum += fileBlock
            remainingSize -= fileBlock
        }
        
        // Double indirect (p2)
        var p2_0 = ppp
        val kp2_0 = ppp
        val kp2_1 = ppp
        while (p2_0 > 0 && remainingSize >= 0) {
            if (p2_0 == kp2_0) systemSum += l1l2l3
            p2_0--
            var p2_1 = ppp
            while (p2_1 > 0 && remainingSize >= 0) {
                if (p2_1 == kp2_1) systemSum += l1l2l3
                fileSum += fileBlock
                remainingSize -= fileBlock
                p2_1--
            }
        }
        
        // Triple indirect (p3)
        var p3_0 = ppp
        val kp3_0 = ppp
        val kp3_1 = ppp
        val kp3_2 = ppp
        while (p3_0 > 0 && remainingSize >= 0) {
            if (p3_0 == kp3_0) systemSum += l1l2l3
            p3_0--
            var p3_1 = ppp
            while (p3_1 > 0 && remainingSize >= 0) {
                if (p3_1 == kp3_1) systemSum += l1l2l3
                p3_1--
                var p3_2 = ppp
                while (p3_2 > 0 && remainingSize >= 0) {
                    if (p3_2 == kp3_2) systemSum += l1l2l3
                    fileSum += fileBlock
                    remainingSize -= fileBlock
                    p3_2--
                }
            }
        }
        
        val systemBlocks = (if (l0 > 0) 1 else 0) + systemSum / l1l2l3
        val fileBlocks = fileSum / fileBlock
        val totalBlocks = systemBlocks + fileBlocks
        val totalBytes = fileSum + systemSum
        
        return PointersResult(
            totalBlocks = totalBlocks,
            systemBytes = systemSum + l0,
            systemBlocks = systemBlocks,
            fileBytes = fileSum,
            fileBlocks = fileBlocks,
            totalBytes = totalBytes,
            directPointers = directPointers,
            pointerSizeBits = pointerSizeBits,
            blockSizeBytes = blockSizeBytes,
            fileSizeBytes = fileSizeBytes,
            pointersPerBlock = ppp
        )
    }
}

package com.arekalov.osrubezh.data.calculator

import com.arekalov.osrubezh.domain.model.NUMAResult
import javax.inject.Inject

class NUMACalculator @Inject constructor() {
    
    fun calculate(
        frequencyGHz: Double,
        l1l2TimeNs: Double,
        localNumaTimeNs: Double,
        otherNumaTimeNs: Double,
        registersCount: Int,
        l1l2Count: Int,
        localNumaCount: Int,
        otherNumaCount: Int
    ): NUMAResult {
        val registerTimeNs = 1.0 / frequencyGHz
        
        val timeBefore = registerTimeNs * registersCount + 
                        l1l2TimeNs * l1l2Count + 
                        localNumaTimeNs * localNumaCount + 
                        otherNumaTimeNs * otherNumaCount
        
        val timeAfter = registerTimeNs * registersCount + 
                       l1l2TimeNs * (l1l2Count + localNumaCount) + 
                       localNumaTimeNs * otherNumaCount
        
        val answer = timeBefore - timeAfter
        
        return NUMAResult(
            answer = answer,
            frequencyGHz = frequencyGHz,
            registerTime = registerTimeNs,
            l1l2Time = l1l2TimeNs,
            localNumaTime = localNumaTimeNs,
            otherNumaTime = otherNumaTimeNs,
            registersBefore = registersCount,
            l1l2Before = l1l2Count,
            localNumaBefore = localNumaCount,
            otherNumaBefore = otherNumaCount,
            registersAfter = registersCount,
            l1l2After = l1l2Count + localNumaCount,
            localNumaAfter = otherNumaCount,
            otherNumaAfter = 0,
            timeBefore = timeBefore,
            timeAfter = timeAfter
        )
    }
}

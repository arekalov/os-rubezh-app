package com.arekalov.osrubezh.data.calculator

import android.util.Log
import com.arekalov.osrubezh.domain.model.FCFSResult
import com.arekalov.osrubezh.domain.model.ProcessResult
import javax.inject.Inject
import kotlin.math.ceil

class FCFSCalculator @Inject constructor() {
    
    fun calculate(
        cpuTime: Int,
        ioTime: Int,
        totalWorkTime: Int,
        processCount: Int
    ): FCFSResult {
        Log.d("FCFSCalc", "=== ВХОДНЫЕ ДАННЫЕ ===")
        Log.d("FCFSCalc", "Время вычислений (CPU): $cpuTime мс")
        Log.d("FCFSCalc", "Время ввода-вывода (I/O): $ioTime мс")
        Log.d("FCFSCalc", "Всего времени работы: $totalWorkTime мс")
        Log.d("FCFSCalc", "Количество процессов: $processCount")
        
        // Количество циклов CPU + I/O
        val cycles = ceil(totalWorkTime.toDouble() / (cpuTime + ioTime)).toInt()
        Log.d("FCFSCalc", "Циклов: $cycles")
        
        // Общее время CPU и I/O для каждого процесса
        val totalCpuTime = cpuTime * cycles
        val totalWaitTime = ioTime * cycles
        
        Log.d("FCFSCalc", "Общее CPU время для процесса: $totalCpuTime мс")
        Log.d("FCFSCalc", "Общее I/O время для процесса: $totalWaitTime мс")
        
        val processes = mutableListOf<ProcessResult>()
        
        for (i in 0 until processCount) {
            // Время ожидания в очереди готовых
            val totalReadyTime = (processCount * cpuTime - (ioTime + cpuTime)) * (cycles - 1) + i * cpuTime
            
            val totalTime = totalCpuTime + totalWaitTime + totalReadyTime
            
            processes.add(
                ProcessResult(
                    processNumber = i + 1,
                    cpuTime = totalCpuTime,
                    waitTime = totalWaitTime,
                    readyTime = totalReadyTime,
                    totalTime = totalTime
                )
            )
            
            Log.d("FCFSCalc", "P${i+1}: cpu=$totalCpuTime, wait=$totalWaitTime, ready=$totalReadyTime, total=$totalTime")
        }
        
        // Максимальное время завершения всех процессов
        val maxTotalTime = processes.maxOf { it.totalTime }
        
        // Минимальное время завершения первого процесса
        val minTotalTime = processes.minOf { it.totalTime }
        
        // Максимальное время ожидания во всех очередях
        val maxWaitInAllQueues = processes.maxOf { it.readyTime } + totalWaitTime
        
        // Минимальное время ожидания во всех очередях
        val minWaitInAllQueues = processes.minOf { it.readyTime } + totalWaitTime
        
        // Максимальное время ожидания в очереди готовых
        val maxReadyQueueWait = processes.maxOf { it.readyTime }
        
        // Минимальное время ожидания в очереди готовых
        val minReadyQueueWait = processes.minOf { it.readyTime }
        
        Log.d("FCFSCalc", "=== РЕЗУЛЬТАТЫ ===")
        Log.d("FCFSCalc", "Завершение всех процессов: $maxTotalTime мс")
        Log.d("FCFSCalc", "Завершение первого процесса: $minTotalTime мс")
        Log.d("FCFSCalc", "Макс. ожидание во всех очередях: $maxWaitInAllQueues мс")
        Log.d("FCFSCalc", "Мин. ожидание во всех очередях: $minWaitInAllQueues мс")
        Log.d("FCFSCalc", "Макс. ожидание в очереди готовых: $maxReadyQueueWait мс")
        Log.d("FCFSCalc", "Мин. ожидание в очереди готовых: $minReadyQueueWait мс")
        
        return FCFSResult(
            processes = processes,
            maxTotalTime = maxTotalTime,
            minTotalTime = minTotalTime,
            maxWaitInAllQueues = maxWaitInAllQueues,
            minWaitInAllQueues = minWaitInAllQueues,
            maxReadyQueueWait = maxReadyQueueWait,
            minReadyQueueWait = minReadyQueueWait,
            cpuTime = cpuTime,
            ioTime = ioTime,
            totalWorkTime = totalWorkTime,
            processCount = processCount,
            cycles = cycles
        )
    }
}

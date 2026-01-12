package com.arekalov.osrubezh.domain.model

import com.arekalov.osrubezh.presentation.utils.NumberFormatter

data class FCFSResult(
    val processes: List<ProcessResult>,
    val maxTotalTime: Int,
    val minTotalTime: Int,
    val maxWaitInAllQueues: Int,
    val minWaitInAllQueues: Int,
    val maxReadyQueueWait: Int,
    val minReadyQueueWait: Int,
    val cpuTime: Int,
    val ioTime: Int,
    val totalWorkTime: Int,
    val processCount: Int,
    val cycles: Int
) {
    fun getFormattedDetails(): String = buildString {
        appendLine("## Параметры")
        appendLine("- Время вычислений (CPU): **${NumberFormatter.format(cpuTime)}** мс")
        appendLine("- Время ввода-вывода (I/O): **${NumberFormatter.format(ioTime)}** мс")
        appendLine("- Всего времени работы: **${NumberFormatter.format(totalWorkTime)}** мс")
        appendLine("- Количество процессов: **${NumberFormatter.format(processCount)}**")
        appendLine("- Циклов CPU+I/O: **${NumberFormatter.format(cycles)}**")
        appendLine("")
        appendLine("## Процессы")
        processes.forEach { process ->
            appendLine("### Процесс P${process.processNumber}")
            appendLine("- CPU время: **${NumberFormatter.format(process.cpuTime)}** мс")
            appendLine("- I/O время (ожидание): **${NumberFormatter.format(process.waitTime)}** мс")
            appendLine("- Время в очереди готовых: **${NumberFormatter.format(process.readyTime)}** мс")
            appendLine("- Общее время: **${NumberFormatter.format(process.totalTime)}** мс")
            appendLine("")
        }
        appendLine("## Результаты")
        appendLine("### Завершение всех процессов:")
        appendLine("**${NumberFormatter.format(maxTotalTime)}** мс")
        appendLine("")
        appendLine("### Завершение первого процесса:")
        appendLine("**${NumberFormatter.format(minTotalTime)}** мс")
        appendLine("")
        appendLine("### Макс. ожидание во всех очередях:")
        appendLine("**${NumberFormatter.format(maxWaitInAllQueues)}** мс")
        appendLine("")
        appendLine("### Мин. ожидание во всех очередях:")
        appendLine("**${NumberFormatter.format(minWaitInAllQueues)}** мс")
        appendLine("")
        appendLine("### Макс. ожидание в очереди готовых:")
        appendLine("**${NumberFormatter.format(maxReadyQueueWait)}** мс")
        appendLine("")
        appendLine("### Мин. ожидание в очереди готовых:")
        appendLine("**${NumberFormatter.format(minReadyQueueWait)}** мс")
    }
}

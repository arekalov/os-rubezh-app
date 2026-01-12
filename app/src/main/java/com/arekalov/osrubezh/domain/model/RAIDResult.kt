package com.arekalov.osrubezh.domain.model

import com.arekalov.osrubezh.presentation.utils.NumberFormatter

data class RAIDResult(
    val answer: Double,
    val answerMinutes: Double,
    val raidLevel: Int,
    val diskCount: Int,
    val validDisks: Int,
    val diskSizeGB: Int,
    val diskSizeBytes: Long,
    val blockSize: Int,
    val suSize: Int,
    val blockRead: Int,
    val blockWrite: Int,
    val suCalc: Int,
    val stripes: Double,
    val blockCount: Long,
    val writeTime: Double,
    val calculateTime: Double,
    val readTime: Double
) {
    fun getFormattedDetails(): String = buildString {
        appendLine("## 1) Определяем размер диска в байтах:")
        appendLine("disk_size = $diskSizeGB * 1024 * 1024 * 1024 = **${NumberFormatter.format(diskSizeBytes)}** байт")
        appendLine("")
        appendLine("## 2) Определяем количество stripe-unit'ов на диске:")
        appendLine("stripes = ${NumberFormatter.format(diskSizeBytes)} / ($blockSize * $suSize) = **${NumberFormatter.format(stripes)}**")
        appendLine("")
        appendLine("## 3) Определяем количество блоков на диске:")
        appendLine("block_count = ${NumberFormatter.format(diskSizeBytes)} / $blockSize = **${NumberFormatter.format(blockCount)}**")
        appendLine("")
        appendLine("### Теперь считаем время (чтение с соседей + вычисление + запись):")
        appendLine("")
        appendLine("## 4) Сколько нам придется писать в блоки:")
        appendLine("write_time = ${NumberFormatter.format(blockCount)} * $blockWrite мкс = **${NumberFormatter.format(writeTime)}** мкс")
        appendLine("")
        appendLine("## 5) Сколько нам придется считать:")
        appendLine("calculate_time = ${NumberFormatter.format(stripes)} * $suCalc мкс = **${NumberFormatter.format(calculateTime)}** мкс")
        appendLine("")
        appendLine("## 6) Сколько нам придется читать:")
        appendLine("read_time = ${NumberFormatter.format(blockCount)} * $blockRead мкс * $validDisks = **${NumberFormatter.format(readTime)}** мкс")
        appendLine("")
        appendLine("## 7) Итого:")
        appendLine("answer = ${NumberFormatter.format(writeTime)} + ${NumberFormatter.format(calculateTime)} + ${NumberFormatter.format(readTime)} = **${NumberFormatter.format(answer)}** мкс = **${NumberFormatter.format(answerMinutes)}** мин")
    }
}

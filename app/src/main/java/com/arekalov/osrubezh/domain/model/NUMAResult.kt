package com.arekalov.osrubezh.domain.model

import com.arekalov.osrubezh.presentation.utils.NumberFormatter

data class NUMAResult(
    val answer: Double,
    val frequencyGHz: Double,
    val registerTime: Double,
    val l1l2Time: Double,
    val localNumaTime: Double,
    val otherNumaTime: Double,
    val registersBefore: Int,
    val l1l2Before: Int,
    val localNumaBefore: Int,
    val otherNumaBefore: Int,
    val registersAfter: Int,
    val l1l2After: Int,
    val localNumaAfter: Int,
    val otherNumaAfter: Int,
    val timeBefore: Double,
    val timeAfter: Double
) {
    fun getFormattedDetails(): String = buildString {
        appendLine("## Параметры системы")
        appendLine("- Частота процессора: **${NumberFormatter.format(frequencyGHz)}** ГГц")
        appendLine("")
        appendLine("### Длительность обращения:")
        appendLine("- **${NumberFormatter.format(registerTime)}** нс - registers")
        appendLine("- **${NumberFormatter.format(l1l2Time)}** нс - L1-L2")
        appendLine("- **${NumberFormatter.format(localNumaTime)}** нс - local NUMA")
        appendLine("- **${NumberFormatter.format(otherNumaTime)}** нс - other NUMA")
        appendLine("")
        appendLine("### Обращений ДО оптимизации:")
        appendLine("- **${NumberFormatter.format(registersBefore)}** - registers")
        appendLine("- **${NumberFormatter.format(l1l2Before)}** - L1-L2")
        appendLine("- **${NumberFormatter.format(localNumaBefore)}** - local NUMA")
        appendLine("- **${NumberFormatter.format(otherNumaBefore)}** - other NUMA")
        appendLine("")
        appendLine("### Обращений ПОСЛЕ оптимизации:")
        appendLine("- **${NumberFormatter.format(registersAfter)}** - registers")
        appendLine("- **${NumberFormatter.format(l1l2After)}** - L1-L2 (локальные данные попали в кеш)")
        appendLine("- **${NumberFormatter.format(localNumaAfter)}** - local NUMA (удаленная память перенесена)")
        appendLine("- **0** - other NUMA")
        appendLine("")
        appendLine("## Расчет времени")
        appendLine("### До оптимизации:")
        appendLine("time_before = ${NumberFormatter.format(registerTime)} * ${NumberFormatter.format(registersBefore)} + ${NumberFormatter.format(l1l2Time)} * ${NumberFormatter.format(l1l2Before)} + ${NumberFormatter.format(localNumaTime)} * ${NumberFormatter.format(localNumaBefore)} + ${NumberFormatter.format(otherNumaTime)} * ${NumberFormatter.format(otherNumaBefore)}")
        appendLine("= **${NumberFormatter.format(timeBefore)}** нс")
        appendLine("")
        appendLine("### После оптимизации:")
        appendLine("time_after = ${NumberFormatter.format(registerTime)} * ${NumberFormatter.format(registersAfter)} + ${NumberFormatter.format(l1l2Time)} * ${NumberFormatter.format(l1l2After)} + ${NumberFormatter.format(localNumaTime)} * ${NumberFormatter.format(localNumaAfter)}")
        appendLine("= **${NumberFormatter.format(timeAfter)}** нс")
        appendLine("")
        appendLine("## Улучшение:")
        appendLine("answer = ${NumberFormatter.format(timeBefore)} - ${NumberFormatter.format(timeAfter)} = **${NumberFormatter.format(answer)}** нс")
    }
}

package com.arekalov.osrubezh.domain.model

import com.arekalov.osrubezh.presentation.utils.NumberFormatter

data class LevelDetails(
    val fileBlocks: Long,
    val systemBlocks: Long,
    val remainingBlocks: Long,
    val formula: String
)

data class PointersResult(
    val totalBlocks: Long,
    val systemBytes: Long,
    val systemBlocks: Long,
    val fileBytes: Long,
    val fileBlocks: Long,
    val totalBytes: Long,
    val directPointers: Int,
    val pointerSizeBits: Int,
    val blockSizeBytes: Int,
    val fileSizeBytes: Long,
    val pointersPerBlock: Int,
    val p0Details: LevelDetails,
    val p1Details: LevelDetails,
    val p2Details: LevelDetails,
    val p3Details: LevelDetails,
    val p4Details: LevelDetails?
) {
    fun getFormattedDetails(): String = buildString {
        appendLine("## Параметры системы")
        appendLine("- Прямых указателей в inode: **$directPointers**")
        appendLine("- Размер указателя: **$pointerSizeBits** бит")
        appendLine("- Размер блока: **${NumberFormatter.format(blockSizeBytes)}** байт")
        appendLine("- Указателей в блоке: **$pointersPerBlock**")
        appendLine("- Размер файла: **${NumberFormatter.format(fileSizeBytes)}** байт")
        appendLine("")
        
        appendLine("## Пошаговый расчет")
        appendLine("")
        
        // p0
        if (p0Details.fileBlocks > 0) {
            appendLine("### p0 (прямые указатели)")
            appendLine("- Для файла: **${NumberFormatter.format(p0Details.fileBlocks)}** блоков")
            appendLine("- Служебных: ${p0Details.formula}")
            appendLine("- Остаток: **${NumberFormatter.format(p0Details.remainingBlocks)}** блоков")
            appendLine("")
        }
        
        // p1
        if (p1Details.fileBlocks > 0) {
            appendLine("### p1 (одинарная косвенность)")
            appendLine("- Для файла: **${NumberFormatter.format(p1Details.fileBlocks)}** блоков")
            appendLine("- Служебных: ${p1Details.formula}")
            appendLine("- Остаток: **${NumberFormatter.format(p1Details.remainingBlocks)}** блоков")
            appendLine("")
        }
        
        // p2
        if (p2Details.fileBlocks > 0) {
            appendLine("### p2 (двойная косвенность)")
            appendLine("- Для файла: **${NumberFormatter.format(p2Details.fileBlocks)}** блоков")
            appendLine("- Служебных: ${p2Details.formula}")
            appendLine("- Остаток: **${NumberFormatter.format(p2Details.remainingBlocks)}** блоков")
            appendLine("")
        }
        
        // p3
        if (p3Details.fileBlocks > 0) {
            appendLine("### p3 (тройная косвенность)")
            appendLine("- Для файла: **${NumberFormatter.format(p3Details.fileBlocks)}** блоков")
            appendLine("- Служебных: ${p3Details.formula}")
            appendLine("- Остаток: **${NumberFormatter.format(p3Details.remainingBlocks)}** блоков")
            appendLine("")
        }
        
        // p4
        if (p4Details != null && p4Details.fileBlocks > 0) {
            appendLine("### p4 (четверная косвенность)")
            appendLine("- Для файла: **${NumberFormatter.format(p4Details.fileBlocks)}** блоков")
            appendLine("- Служебных: ${p4Details.formula}")
            appendLine("- Остаток: **${NumberFormatter.format(p4Details.remainingBlocks)}** блоков")
            appendLine("")
        }
        
        appendLine("## Итого")
        appendLine("- Блоков для файла: **${NumberFormatter.format(fileBlocks)}**")
        val systemBlocksFormula = buildString {
            append("p0=${p0Details.systemBlocks} + p1=${p1Details.systemBlocks} + p2=${p2Details.systemBlocks} + p3=${p3Details.systemBlocks}")
            if (p4Details != null && p4Details.systemBlocks > 0) {
                append(" + p4=${p4Details.systemBlocks}")
            }
        }
        appendLine("- Служебных блоков: **${NumberFormatter.format(systemBlocks)}** ($systemBlocksFormula)")
        appendLine("- Всего блоков: **${NumberFormatter.format(totalBlocks)}**")
        appendLine("")
        appendLine("## ОТВЕТ: ${NumberFormatter.format(totalBlocks)}")
    }
}

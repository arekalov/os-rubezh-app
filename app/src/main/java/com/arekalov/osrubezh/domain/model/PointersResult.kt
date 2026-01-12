package com.arekalov.osrubezh.domain.model

import com.arekalov.osrubezh.presentation.utils.NumberFormatter

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
    val pointersPerBlock: Int
) {
    fun getFormattedDetails(): String = buildString {
        appendLine("## Параметры системы")
        appendLine("- Прямых указателей в inode: **$directPointers**")
        appendLine("- Размер указателя: **$pointerSizeBits** бит")
        appendLine("- Размер блока: **$blockSizeBytes** байт")
        appendLine("- Указателей в блоке: **$pointersPerBlock**")
        appendLine("- Размер файла: **${NumberFormatter.format(fileSizeBytes)}** байт")
        appendLine("")
        appendLine("## Результаты")
        appendLine("- Системных данных: **${NumberFormatter.format(systemBytes)}** байт (**${NumberFormatter.format(systemBlocks)}** блоков)")
        appendLine("- Файл: **${NumberFormatter.format(fileBytes)}** байт (**${NumberFormatter.format(fileBlocks)}** блоков)")
        appendLine("- Суммарно: **${NumberFormatter.format(totalBytes)}** байт (**${NumberFormatter.format(totalBlocks)}** блоков)")
        appendLine("")
        appendLine("## ОТВЕТ: ${NumberFormatter.format(totalBlocks)}")
    }
}

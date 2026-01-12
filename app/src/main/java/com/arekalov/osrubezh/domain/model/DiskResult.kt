package com.arekalov.osrubezh.domain.model

import com.arekalov.osrubezh.presentation.utils.NumberFormatter

data class DiskResult(
    val sectorsForFile: Int,
    val tracksForFile: Int,
    val realSizeFile: Int,
    val sectorsOnLastTrack: Int,
    val totalFragmentation: Int,
    val bytePerSector: Int,
    val sectorsPerTrack: Int,
    val tracksPerSurface: Int,
    val surfacesPerDisk: Int,
    val recordsCount: Int,
    val bytesPerRecord: Int,
    val recordsInOneSector: Int,
    val bytePerTrack: Int,
    val bytePerSurface: Int,
    val bytePerDisk: Int,
    val fileSize: Int,
    val usefulBytesPerSector: Int,
    val fragmentationPerSector: Int
) {
    fun getFormattedDetails(): String = buildString {
        appendLine("## Параметры диска")
        appendLine("- Байт в секторе: **${NumberFormatter.format(bytePerSector)}**")
        appendLine("- Секторов на дорожке: **${NumberFormatter.format(sectorsPerTrack)}**")
        appendLine("- Дорожек на поверхности: **${NumberFormatter.format(tracksPerSurface)}**")
        appendLine("- Поверхностей на диске: **${NumberFormatter.format(surfacesPerDisk)}**")
        appendLine("")
        appendLine("## Параметры файла")
        appendLine("- Записей: **${NumberFormatter.format(recordsCount)}**")
        appendLine("- Байт в записи: **${NumberFormatter.format(bytesPerRecord)}**")
        appendLine("- Размер файла: **${NumberFormatter.format(fileSize)}** байт")
        appendLine("")
        appendLine("## Расчеты")
        appendLine("### Записей в одном секторе:")
        appendLine("records_per_sector = $bytePerSector / $bytesPerRecord = **${NumberFormatter.format(recordsInOneSector)}**")
        appendLine("")
        appendLine("### Байт на дорожке:")
        appendLine("bytes_per_track = $bytePerSector * $sectorsPerTrack = **${NumberFormatter.format(bytePerTrack)}**")
        appendLine("")
        appendLine("### Байт на поверхности:")
        appendLine("bytes_per_surface = $bytePerTrack * $tracksPerSurface = **${NumberFormatter.format(bytePerSurface)}**")
        appendLine("")
        appendLine("### Байт на диске:")
        appendLine("bytes_per_disk = $bytePerSurface * $surfacesPerDisk = **${NumberFormatter.format(bytePerDisk)}**")
        appendLine("")
        appendLine("## Ответы")
        appendLine("### 1) Количество секторов для файла:")
        appendLine("sectors = ceil($recordsCount / $recordsInOneSector) = **${NumberFormatter.format(sectorsForFile)}**")
        appendLine("")
        appendLine("### 2) Количество дорожек для файла:")
        appendLine("tracks = ceil($sectorsForFile / $sectorsPerTrack) = **${NumberFormatter.format(tracksForFile)}**")
        appendLine("")
        appendLine("### 3) Реальный размер файла с фрагментацией:")
        appendLine("real_size = $sectorsForFile * $bytePerSector = **${NumberFormatter.format(realSizeFile)}** байт")
        appendLine("")
        appendLine("### 4) Секторов на последней дорожке:")
        appendLine("last_track_sectors = $sectorsForFile mod $sectorsPerTrack = **${NumberFormatter.format(sectorsOnLastTrack)}**")
        appendLine("")
        appendLine("## Задание 3: Фрагментация")
        appendLine("### Полезных байт в секторе:")
        appendLine("useful = $recordsInOneSector × $bytesPerRecord = **${NumberFormatter.format(usefulBytesPerSector)}** байт")
        appendLine("")
        appendLine("### Фрагментация в одном секторе:")
        appendLine("frag_per_sector = $bytePerSector - $usefulBytesPerSector = **${NumberFormatter.format(fragmentationPerSector)}** байт")
        appendLine("")
        appendLine("### Внутренняя фрагментация:")
        appendLine("total_frag = ${sectorsForFile - 1} × $fragmentationPerSector + остаток")
        appendLine("total_frag = **${NumberFormatter.format(totalFragmentation)}** байт")
    }
}

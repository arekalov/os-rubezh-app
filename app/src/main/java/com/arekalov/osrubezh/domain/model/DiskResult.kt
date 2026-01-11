package com.arekalov.osrubezh.domain.model

data class DiskResult(
    val sectorsForFile: Int,
    val tracksForFile: Int,
    val realSizeFile: Int,
    val sectorsOnLastTrack: Int,
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
    val fileSize: Int
)

enum class DiskQuestionType(val title: String) {
    SECTORS("Количество секторов"),
    TRACKS("Количество дорожек"),
    FILE_SIZE("Размер файла с фрагментацией"),
    LAST_TRACK("Секторов на последней дорожке")
}

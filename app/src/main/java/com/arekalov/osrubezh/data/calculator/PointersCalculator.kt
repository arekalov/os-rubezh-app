package com.arekalov.osrubezh.data.calculator

import android.util.Log
import com.arekalov.osrubezh.domain.model.PointersResult
import javax.inject.Inject
import kotlin.math.ceil

class PointersCalculator @Inject constructor() {
    
    fun calculate(
        directPointers: Int,
        pointerSizeBits: Int,
        blockSizeBytes: Int,
        fileSizeBytes: Long
    ): PointersResult {
        Log.d("PointersCalc", "=== ВХОДНЫЕ ДАННЫЕ ===")
        Log.d("PointersCalc", "Прямых указателей: $directPointers")
        Log.d("PointersCalc", "Размер указателя: $pointerSizeBits бит")
        Log.d("PointersCalc", "Размер блока: $blockSizeBytes байт")
        Log.d("PointersCalc", "Размер файла: $fileSizeBytes байт")
        
        // 1) Определяем количество блоков, в которые уместится файл
        val totalFileBlocks = ceil(fileSizeBytes.toDouble() / blockSizeBytes).toLong()
        Log.d("PointersCalc", "Всего блоков для файла: $totalFileBlocks")
        
        // Указателей в одном блоке
        val pointersPerBlock = (blockSizeBytes * 8) / pointerSizeBits
        Log.d("PointersCalc", "Указателей в блоке: $pointersPerBlock")
        
        // 2) Считаем уровни
        var remaining = totalFileBlocks
        
        // p0: прямые указатели
        val p0FileBlocks = minOf(remaining, directPointers.toLong())
        val sum_p0 = if (p0FileBlocks > 0) 1L else 0L  // 1 служебный блок (сам inode)
        remaining -= p0FileBlocks
        Log.d("PointersCalc", "p0: для файла=$p0FileBlocks, служебных=$sum_p0, осталось=$remaining")
        
        // p1: одинарная косвенность
        val p1FileBlocks = minOf(remaining, pointersPerBlock.toLong())
        val sum_p1 = if (p1FileBlocks > 0) 1L else 0L  // 1 служебный блок
        remaining -= p1FileBlocks
        Log.d("PointersCalc", "p1: для файла=$p1FileBlocks, служебных=$sum_p1, осталось=$remaining")
        
        // p2: двойная косвенность
        val p2MaxBlocks = pointersPerBlock.toLong() * pointersPerBlock
        val p2FileBlocks = minOf(remaining, p2MaxBlocks)
        val sum_p2 = if (p2FileBlocks > 0) {
            val l2Blocks = ceil(p2FileBlocks.toDouble() / pointersPerBlock).toLong()
            l2Blocks + 1  // блоки L2 + 1 блок L1
        } else {
            0L
        }
        remaining -= p2FileBlocks
        Log.d("PointersCalc", "p2: для файла=$p2FileBlocks, служебных=$sum_p2, осталось=$remaining")
        
        // p3: тройная косвенность
        val sum_p3 = if (remaining > 0) {
            val p3FileBlocks = remaining
            
            // Сколько блоков L2 нужно полностью заполнить
            val fullL2Blocks = (p3FileBlocks / p2MaxBlocks).toInt()
            var p3Remaining = p3FileBlocks % p2MaxBlocks
            
            // Сколько блоков L3 нужно для остатка
            val partialL2Blocks = if (p3Remaining > 0) 1 else 0
            val l3BlocksInPartial = if (p3Remaining > 0) {
                ceil(p3Remaining.toDouble() / pointersPerBlock).toLong()
            } else {
                0L
            }
            
            // Общее количество служебных блоков:
            // - 1 блок на L1 (сам p3)
            // - fullL2Blocks + partialL2Blocks блоков на L2
            // - fullL2Blocks * pointersPerBlock блоков L3 для полных L2
            // - l3BlocksInPartial блоков L3 для частичного L2
            val l1Blocks = 1L
            val l2Blocks = (fullL2Blocks + partialL2Blocks).toLong()
            val l3Blocks = fullL2Blocks * pointersPerBlock + l3BlocksInPartial
            
            Log.d("PointersCalc", "p3: L1=$l1Blocks, L2=$l2Blocks, L3=$l3Blocks")
            Log.d("PointersCalc", "p3: полных L2=$fullL2Blocks, частичных L2=$partialL2Blocks")
            
            l1Blocks + l2Blocks + l3Blocks
        } else {
            0L
        }
        
        Log.d("PointersCalc", "p3: служебных=$sum_p3")
        
        // 3) Суммируем
        val totalSystemBlocks = sum_p0 + sum_p1 + sum_p2 + sum_p3
        val totalBlocks = totalFileBlocks + totalSystemBlocks
        
        Log.d("PointersCalc", "=== ИТОГО ===")
        Log.d("PointersCalc", "Блоков для файла: $totalFileBlocks")
        Log.d("PointersCalc", "Служебных блоков: $totalSystemBlocks (p0=$sum_p0, p1=$sum_p1, p2=$sum_p2, p3=$sum_p3)")
        Log.d("PointersCalc", "Всего блоков: $totalBlocks")
        
        return PointersResult(
            totalBlocks = totalBlocks,
            systemBytes = totalSystemBlocks * blockSizeBytes,
            systemBlocks = totalSystemBlocks,
            fileBytes = totalFileBlocks * blockSizeBytes,
            fileBlocks = totalFileBlocks,
            totalBytes = totalBlocks * blockSizeBytes,
            directPointers = directPointers,
            pointerSizeBits = pointerSizeBits,
            blockSizeBytes = blockSizeBytes,
            fileSizeBytes = fileSizeBytes,
            pointersPerBlock = pointersPerBlock
        )
    }
}

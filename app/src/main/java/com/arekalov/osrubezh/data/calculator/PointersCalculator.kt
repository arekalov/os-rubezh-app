package com.arekalov.osrubezh.data.calculator

import android.util.Log
import com.arekalov.osrubezh.domain.model.LevelDetails
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
        val sum_p0 = if (p0FileBlocks > 0) 1L else 0L
        remaining -= p0FileBlocks
        
        val p0Details = LevelDetails(
            fileBlocks = p0FileBlocks,
            systemBlocks = sum_p0,
            remainingBlocks = remaining,
            formula = if (p0FileBlocks > 0) "**1** блок (inode)" else "**0** блоков"
        )
        
        Log.d("PointersCalc", "p0: для файла=$p0FileBlocks, служебных=$sum_p0, осталось=$remaining")
        
        // p1: одинарная косвенность
        val p1FileBlocks = minOf(remaining, pointersPerBlock.toLong())
        val sum_p1 = if (p1FileBlocks > 0) 1L else 0L
        remaining -= p1FileBlocks
        
        val p1Details = LevelDetails(
            fileBlocks = p1FileBlocks,
            systemBlocks = sum_p1,
            remainingBlocks = remaining,
            formula = if (p1FileBlocks > 0) "**1** блок (L1)" else "**0** блоков"
        )
        
        Log.d("PointersCalc", "p1: для файла=$p1FileBlocks, служебных=$sum_p1, осталось=$remaining")
        
        // p2: двойная косвенность
        val p2MaxBlocks = pointersPerBlock.toLong() * pointersPerBlock
        val p2FileBlocks = minOf(remaining, p2MaxBlocks)
        
        val (sum_p2, p2L1Blocks, p2L2Blocks) = if (p2FileBlocks > 0) {
            val l2Blocks = ceil(p2FileBlocks.toDouble() / pointersPerBlock).toLong()
            Triple(l2Blocks + 1, 1L, l2Blocks)
        } else {
            Triple(0L, 0L, 0L)
        }
        remaining -= p2FileBlocks
        
        val p2Details = LevelDetails(
            fileBlocks = p2FileBlocks,
            systemBlocks = sum_p2,
            remainingBlocks = remaining,
            formula = if (p2FileBlocks > 0) {
                "**$p2L1Blocks** блок L1 + **$p2L2Blocks** блоков L2 = **$sum_p2** блоков"
            } else {
                "**0** блоков"
            }
        )
        
        Log.d("PointersCalc", "p2: для файла=$p2FileBlocks, служебных=$sum_p2, осталось=$remaining")
        
        // p3: тройная косвенность
        val p3MaxBlocks = pointersPerBlock.toLong() * pointersPerBlock * pointersPerBlock
        val p3FileBlocks = minOf(remaining, p3MaxBlocks)
        
        val (sum_p3, p3Formula) = if (p3FileBlocks > 0) {
            // Сколько блоков уровня p2 (L2) нужно полностью заполнить
            val fullL2Blocks = (p3FileBlocks / p2MaxBlocks).toInt()
            val p3Remaining = p3FileBlocks % p2MaxBlocks
            
            // Сколько блоков L3 нужно для остатка
            val partialL2Blocks = if (p3Remaining > 0) 1 else 0
            val l3BlocksInPartial = if (p3Remaining > 0) {
                ceil(p3Remaining.toDouble() / pointersPerBlock).toLong()
            } else {
                0L
            }
            
            val l1Blocks = 1L
            val l2Blocks = (fullL2Blocks + partialL2Blocks).toLong()
            val l3Blocks = fullL2Blocks * pointersPerBlock + l3BlocksInPartial
            val totalP3 = l1Blocks + l2Blocks + l3Blocks
            
            val formula = buildString {
                append("**$l1Blocks** блок L1 + **$l2Blocks** блоков L2")
                if (fullL2Blocks > 0 && partialL2Blocks > 0) {
                    append(" ($fullL2Blocks полных + $partialL2Blocks частичный)")
                } else if (fullL2Blocks > 0) {
                    append(" ($fullL2Blocks полных)")
                } else if (partialL2Blocks > 0) {
                    append(" ($partialL2Blocks частичный)")
                }
                append(" + **$l3Blocks** блоков L3")
                if (fullL2Blocks > 0 && partialL2Blocks > 0) {
                    append(" ($fullL2Blocks × $pointersPerBlock + $l3BlocksInPartial)")
                } else if (fullL2Blocks > 0) {
                    append(" ($fullL2Blocks × $pointersPerBlock)")
                } else {
                    append(" ($l3BlocksInPartial)")
                }
                append(" = **$totalP3** блоков")
            }
            
            Log.d("PointersCalc", "p3: L1=$l1Blocks, L2=$l2Blocks, L3=$l3Blocks")
            Log.d("PointersCalc", "p3: полных L2=$fullL2Blocks, частичных L2=$partialL2Blocks")
            
            Pair(totalP3, formula)
        } else {
            Pair(0L, "**0** блоков")
        }
        
        remaining -= p3FileBlocks
        
        val p3Details = LevelDetails(
            fileBlocks = p3FileBlocks,
            systemBlocks = sum_p3,
            remainingBlocks = remaining,
            formula = p3Formula
        )
        
        Log.d("PointersCalc", "p3: для файла=$p3FileBlocks, служебных=$sum_p3, осталось=$remaining")
        
        // p4: четверная косвенность
        val p4FileBlocks = remaining
        val (sum_p4, p4Formula) = if (p4FileBlocks > 0) {
            // p4 может вместить pointersPerBlock блоков p3
            // Каждый блок p3 может вместить p3MaxBlocks данных
            
            // Сколько полных блоков p3 нужно
            val fullP3Blocks = (p4FileBlocks / p3MaxBlocks).toInt()
            val p4Remaining = p4FileBlocks % p3MaxBlocks
            
            // Сколько блоков L3 и L2 нужно для остатка
            val partialP3Blocks = if (p4Remaining > 0) 1 else 0
            
            // Для частичного p3 блока считаем L2 и L3
            val (partialL2, partialL3) = if (p4Remaining > 0) {
                val fullL2InPartial = (p4Remaining / p2MaxBlocks).toInt()
                val lastL2Remaining = p4Remaining % p2MaxBlocks
                val partialL2InPartial = if (lastL2Remaining > 0) 1 else 0
                val l2Total = fullL2InPartial + partialL2InPartial
                
                val l3InFull = fullL2InPartial * pointersPerBlock
                val l3InPartial = if (lastL2Remaining > 0) {
                    ceil(lastL2Remaining.toDouble() / pointersPerBlock).toLong()
                } else {
                    0L
                }
                val l3Total = l3InFull + l3InPartial
                
                Pair(l2Total.toLong(), l3Total)
            } else {
                Pair(0L, 0L)
            }
            
            val l1Blocks = 1L // сам p4
            val l2Blocks = (fullP3Blocks + partialP3Blocks).toLong() // указатели на p3
            val l3Blocks = fullP3Blocks * pointersPerBlock + partialL2 // указатели на p2
            val l4Blocks = fullP3Blocks * pointersPerBlock * pointersPerBlock + partialL3 // указатели на p1
            val totalP4 = l1Blocks + l2Blocks + l3Blocks + l4Blocks
            
            val formula = buildString {
                append("**$l1Blocks** блок L1 + **$l2Blocks** блоков L2")
                if (fullP3Blocks > 0 && partialP3Blocks > 0) {
                    append(" ($fullP3Blocks полных + $partialP3Blocks частичный)")
                } else if (fullP3Blocks > 0) {
                    append(" ($fullP3Blocks полных)")
                } else if (partialP3Blocks > 0) {
                    append(" ($partialP3Blocks частичный)")
                }
                append(" + **$l3Blocks** блоков L3")
                if (fullP3Blocks > 0) {
                    append(" ($fullP3Blocks × $pointersPerBlock")
                    if (partialL2 > 0) append(" + $partialL2")
                    append(")")
                } else if (partialL2 > 0) {
                    append(" ($partialL2)")
                }
                append(" + **$l4Blocks** блоков L4")
                if (fullP3Blocks > 0) {
                    append(" ($fullP3Blocks × ${pointersPerBlock * pointersPerBlock}")
                    if (partialL3 > 0) append(" + $partialL3")
                    append(")")
                } else if (partialL3 > 0) {
                    append(" ($partialL3)")
                }
                append(" = **$totalP4** блоков")
            }
            
            Log.d("PointersCalc", "p4: L1=$l1Blocks, L2=$l2Blocks, L3=$l3Blocks, L4=$l4Blocks")
            Log.d("PointersCalc", "p4: полных p3=$fullP3Blocks, частичных p3=$partialP3Blocks")
            
            Pair(totalP4, formula)
        } else {
            Pair(0L, "**0** блоков")
        }
        
        remaining -= p4FileBlocks
        
        val p4Details = if (p4FileBlocks > 0) {
            LevelDetails(
                fileBlocks = p4FileBlocks,
                systemBlocks = sum_p4,
                remainingBlocks = 0,
                formula = p4Formula
            )
        } else {
            null
        }
        
        Log.d("PointersCalc", "p4: для файла=$p4FileBlocks, служебных=$sum_p4, осталось=$remaining")
        
        // 3) Суммируем
        val totalSystemBlocks = sum_p0 + sum_p1 + sum_p2 + sum_p3 + sum_p4
        val totalBlocks = totalFileBlocks + totalSystemBlocks
        
        Log.d("PointersCalc", "=== ИТОГО ===")
        Log.d("PointersCalc", "Блоков для файла: $totalFileBlocks")
        Log.d("PointersCalc", "Служебных блоков: $totalSystemBlocks (p0=$sum_p0, p1=$sum_p1, p2=$sum_p2, p3=$sum_p3, p4=$sum_p4)")
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
            pointersPerBlock = pointersPerBlock,
            p0Details = p0Details,
            p1Details = p1Details,
            p2Details = p2Details,
            p3Details = p3Details,
            p4Details = p4Details
        )
    }
}

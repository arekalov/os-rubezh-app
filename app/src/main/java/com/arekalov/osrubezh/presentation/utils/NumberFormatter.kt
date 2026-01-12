package com.arekalov.osrubezh.presentation.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberFormatter {
    private val formatter = DecimalFormat("#,###").apply {
        decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
            groupingSeparator = ' '
        }
    }
    
    fun format(number: Long): String {
        return formatter.format(number)
    }
    
    fun format(number: Int): String {
        return formatter.format(number)
    }
    
    fun format(number: Double): String {
        return if (number == number.toLong().toDouble()) {
            formatter.format(number.toLong())
        } else {
            val df = DecimalFormat("#,###.#######").apply {
                decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
                    groupingSeparator = ' '
                    decimalSeparator = '.'
                }
            }
            df.format(number).trimEnd('0').trimEnd('.')
        }
    }
}

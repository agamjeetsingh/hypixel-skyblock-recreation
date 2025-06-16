package firstplugin.skyblock.utils

import java.text.NumberFormat
import java.util.Locale

object NumberFormat {
    private val formatter = NumberFormat.getNumberInstance(Locale.US)
    
    /**
     * Formats a number with commas as thousand separators.
     * For example: 250000 becomes "250,000"
     * 
     * @param number The number to format
     * @return The formatted number as a string
     */
    fun formatWithCommas(number: Int): String {
        return formatter.format(number)
    }
    
    /**
     * Formats a long number with commas as thousand separators.
     * For example: 1250000 becomes "1,250,000"
     * 
     * @param number The number to format
     * @return The formatted number as a string
     */
    fun formatWithCommas(number: Long): String {
        return formatter.format(number)
    }
    
    /**
     * Formats a double number with commas as thousand separators.
     * For example: 1250000.5 becomes "1,250,000.5"
     * 
     * @param number The number to format
     * @param fractionDigits The number of decimal places to show (default is 1)
     * @return The formatted number as a string
     */
    fun formatWithCommas(number: Double, fractionDigits: Int = 1): String {
        formatter.maximumFractionDigits = fractionDigits
        formatter.minimumFractionDigits = fractionDigits
        return formatter.format(number)
    }
}
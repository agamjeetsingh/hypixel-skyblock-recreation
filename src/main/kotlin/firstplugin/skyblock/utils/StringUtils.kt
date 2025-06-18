package firstplugin.skyblock.utils

/**
 * Utility functions for string manipulation and formatting
 */
object StringUtils {
    /**
     * Converts a camel case string to a spaced string.
     *
     * Examples:
     * - "CritChance" -> "Crit Chance"
     * - "MaxHealth" -> "Max Health"
     * - "bonusAttackSpeed" -> "Bonus Attack Speed"
     *
     * @param camelCase The camel case string to convert
     * @return A string with spaces inserted between words
     */
    fun camelCaseToSpaced(camelCase: String): String {
        if (camelCase.isEmpty()) return ""

        // Regex that finds capital letters (except at the beginning of the string)
        // and inserts a space before them
        val result = camelCase.replace(Regex("(?<!^)([A-Z])"), " $1")

        // Handle cases where the first word starts with a lowercase letter
        return if (camelCase[0].isLowerCase()) {
            result.capitalize()
        } else {
            result
        }
    }

    /**
     * Capitalizes the first letter of each word in a string.
     *
     * @param str The string to capitalize
     * @return The string with each word capitalized
     */
    fun capitalizeWords(str: String): String {
        if (str.isEmpty()) return ""

        return str
            .split(" ")
            .joinToString(" ") { word ->
                if (word.isNotEmpty()) {
                    word.substring(0, 1).uppercase() + word.substring(1)
                } else {
                    ""
                }
            }
    }

    /**
     * Returns the last two characters of the english version of an integer.
     *
     * @param num The integer to be converted
     */
    fun intToTh(num: Int): String =
        if (num % 10 == 1 && num % 100 != 11) {
            "st"
        } else if (num % 10 == 2 && num % 100 != 12) {
            "nd"
        } else {
            "th"
        }
}

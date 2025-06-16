package firstplugin.skyblock.utils

object RomanNumerals {
    private val values =
        listOf(
            1000 to "M",
            900 to "CM",
            500 to "D",
            400 to "CD",
            100 to "C",
            90 to "XC",
            50 to "L",
            40 to "XL",
            10 to "X",
            9 to "IX",
            5 to "V",
            4 to "IV",
            1 to "I",
        )

    /**
     * Converts an integer to a Roman numeral string
     * @param number The number to convert (should be positive)
     * @return The Roman numeral representation as a string
     */
    fun toRoman(number: Int): String {
        if (number <= 0) return ""

        var remaining = number
        val result = StringBuilder()

        for ((value, symbol) in values) {
            while (remaining >= value) {
                result.append(symbol)
                remaining -= value
            }
        }

        return result.toString()
    }
}

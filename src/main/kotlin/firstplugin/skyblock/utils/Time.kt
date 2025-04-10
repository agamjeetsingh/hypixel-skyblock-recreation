package firstplugin.skyblock.utils

object Time {
    private const val TICKS_PER_SECOND = 20L
    private const val SECONDS_PER_MINUTE = 60L
    private const val MINUTES_PER_HOUR = 60L
    private const val HOURS_PER_DAY = 24L

    private const val TICKS_PER_MINUTE = TICKS_PER_SECOND * SECONDS_PER_MINUTE // 1_200L
    private const val TICKS_PER_HOUR = TICKS_PER_MINUTE * MINUTES_PER_HOUR // 72_000L
    private const val TICKS_PER_DAY = TICKS_PER_HOUR * HOURS_PER_DAY // 1_728_000L

    fun secondsToTicks(seconds: Long): Long = seconds * TICKS_PER_SECOND

    fun secondsToTicks(seconds: Double): Long = (seconds * TICKS_PER_SECOND).toLong() // Rounds towards zero

    fun minutesToTicks(minutes: Long): Long = minutes * TICKS_PER_MINUTE

    fun minutesToTicks(minutes: Double): Long = (minutes * TICKS_PER_MINUTE).toLong()

    fun hoursToTicks(hours: Long): Long = hours * TICKS_PER_HOUR

    fun hoursToTicks(hours: Double): Long = (hours * TICKS_PER_HOUR).toLong()

    fun daysToTicks(days: Long): Long = days * TICKS_PER_DAY

    fun daysToTicks(days: Double): Long = (days * TICKS_PER_DAY).toLong()

    /**
     * Converts a given number of ticks into a human-readable string representation
     * using the largest appropriate time unit (days, hours, minutes, or seconds).
     * The value is rounded down (integer part only).
     *
     * Examples:
     * - ticksToString(0) -> "0 seconds"
     * - ticksToString(19) -> "0 seconds" (rounds down)
     * - ticksToString(20) -> "1 second"
     * - ticksToString(39) -> "1 second"
     * - ticksToString(40) -> "2 seconds"
     * - ticksToString(1199) -> "59 seconds"
     * - ticksToString(1200) -> "1 minute"
     * - ticksToString(71999) -> "59 minutes"
     * - ticksToString(72000) -> "1 hour"
     * - ticksToString(1727999) -> "23 hours"
     * - ticksToString(1728000) -> "1 day"
     * - ticksToString(3456000) -> "2 days"
     * - ticksToString(54000, capitalize = true) -> "45 Minutes" // 54000 ticks = 45 minutes
     * - ticksToString(72000, capitalize = true) -> "1 Hour"
     *
     * @param ticks The total number of ticks (non-negative).
     * @param capitalize If true, the time unit will be capitalized (e.g., "Seconds", "Minute"). Defaults to false.
     * @return A formatted string like "X unit" or "X units" (e.g., "5 Minutes", "1 second"),
     * or "0 seconds" / "0 Seconds" if ticks are less than TICKS_PER_SECOND.
     * Returns "Invalid Ticks" if input is negative.
     */
    fun ticksToString(
        ticks: Long,
        capitalize: Boolean = false,
    ): String {
        // Handle edge case for negative input
        if (ticks < 0) {
            return "Invalid Ticks: $ticks"
        }

        val value: Long
        val unitNameBase: String // e.g., "second", "Minute"

        // Determine the largest appropriate unit and calculate the value
        when {
            // Use days if ticks represent at least one full day
            ticks >= TICKS_PER_DAY -> {
                value = ticks / TICKS_PER_DAY // Integer division performs floor automatically
                unitNameBase = if (capitalize) "Day" else "day"
            }
            // Use hours if ticks represent at least one full hour
            ticks >= TICKS_PER_HOUR -> {
                value = ticks / TICKS_PER_HOUR
                unitNameBase = if (capitalize) "Hour" else "hour"
            }
            // Use minutes if ticks represent at least one full minute
            ticks >= TICKS_PER_MINUTE -> {
                value = ticks / TICKS_PER_MINUTE
                unitNameBase = if (capitalize) "Minute" else "minute"
            }
            // Otherwise, use seconds (including the case where ticks < TICKS_PER_SECOND, resulting in 0 seconds)
            else -> {
                value = ticks / TICKS_PER_SECOND
                unitNameBase = if (capitalize) "Second" else "second"
            }
        }

        // Add 's' for pluralization if the value is not 1
        val finalUnitName = if (value == 1L) unitNameBase else unitNameBase + "s"

        // Combine value and unit name into the final string
        return "$value $finalUnitName"
    }
}

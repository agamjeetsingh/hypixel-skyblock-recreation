package firstplugin.skyblock.time

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.utils.StringUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.scheduler.BukkitRunnable

/**
 * Represents Skyblock's Clock. 10 Minutes in-game time is about 8.33 seconds in wall clock time.
 * Every month has 31 days and every year has 4 seasons that last 3 months each.
 *
 * @property minutes The number of minutes that have passed since the new hour. Always between 0 and 59 inclusive.
 * @property hours The number of hours that have passed since the new day. Always between 0 and 23 inclusive.
 * @property days The number of days that have passed since the new month. Always between 0 and 30 inclusive.
 * @property months The number of months that have passed since the new year. Always between 0 and 11 inclusive.
 * @property seasons The number of seasons that have passed since the new year. Always between 0 and 3 inclusive.
 * @property years The number of years that have passed since the clock began.
 * @property seasonComponent The Season and Date Component to be displayed on the scoreboard.
 * @property timeComponent The Time Component to be displayed on the scoreboard.
 */
object Clock {
    var minutes: Int = 0
        private set
    var hours: Int = 0
        private set
    var days: Int = 0
        private set
    var months: Int = 0
        private set
    var seasons: Int = 0
        private set
    var years: Int = 0
        private set

    /**
     * This function should be used to set the time when the server is restarted.
     */
    fun setTime(
        minutes: Int? = null,
        hours: Int? = null,
        days: Int? = null,
        months: Int? = null,
        seasons: Int? = null,
        years: Int? = null,
    ) {
        if (minutes != null && minutes >= 0 && minutes < 60) {
            this.minutes = minutes
        }
        if (hours != null && hours >= 0 && hours < 24) {
            this.hours = hours
        }
        if (days != null && days >= 0 && days < 31) {
            this.days = days
        }
        if (months != null && months >= 0 && months < 12) {
            this.months = months
        }
        if (seasons != null && seasons >= 0 && seasons < 4) {
            this.seasons = seasons
        }
        if (years != null && years >= 0) {
            this.years = years
        }
    }

    private val clockRunnable =
        object : BukkitRunnable() {
            override fun run() {
                minutes += 10
                if (minutes == 60) {
                    minutes = 0
                    hours++
                }
                if (hours == 24) {
                    hours = 0
                    days++
                }
                if (days == 31) {
                    days = 0
                    months++
                    seasons = months / 3
                }
                if (seasons == 4) {
                    seasons = 0
                    months = 0
                    years++
                }
            }
        }

    /**
     * Method that starts the clock.
     */
    fun start(plugin: SkyblockPlugin) {
        clockRunnable.runTaskTimer(plugin, 0L, INGAME_10_MINUTES_IN_TICKS)
    }

    val seasonComponent: Component
        get() {
            val seasonSectionString =
                StringUtils.capitalizeWords(
                    SeasonSection.entries
                        .toTypedArray()[seasons.toInt() % 3]
                        .toString()
                        .lowercase(),
                )
            val seasonString =
                StringUtils.capitalizeWords(
                    Season.entries
                        .toTypedArray()[seasons.toInt()]
                        .toString()
                        .lowercase(),
                )
            val thString = StringUtils.intToTh(days.toInt())
            return Component
                .text(" $seasonSectionString $seasonString $days$thString")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
        }

    val timeComponent: Component
        get() {
            val hour: Int = hours % 12
            val amPm: String = if (hours / 12 == 0) "am" else "pm"
            return Component
                .text(" $hour:$minutes$amPm ")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(timeSymbolComponent)
        }

    private const val INGAME_10_MINUTES_IN_TICKS: Long = 167

    private const val DAY_SYMBOL = "☀"

    private const val NIGHT_SYMBOL = "☽"

    private val dayTimeInHours = 5..17

    private val timeSymbolComponent: Component
        get() {
            return if (hours in dayTimeInHours) {
                Component
                    .text(DAY_SYMBOL)
                    .color(NamedTextColor.YELLOW)
                    .decoration(TextDecoration.ITALIC, false)
            } else {
                Component
                    .text(NIGHT_SYMBOL)
                    .color(NamedTextColor.AQUA)
                    .decoration(TextDecoration.ITALIC, false)
            }
        }
}

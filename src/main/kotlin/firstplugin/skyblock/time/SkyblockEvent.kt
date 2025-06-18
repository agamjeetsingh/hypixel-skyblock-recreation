package firstplugin.skyblock.time

import firstplugin.plugin.SkyblockPlugin
import org.bukkit.scheduler.BukkitRunnable

/**
 * Represents events in Skyblock like the New Year Celebration or Jacob's Farming Contest
 *
 * Call the [track] method to start checking whether the events should start or stop. Call [stopTracking] to stop
 * tracking.
 */
abstract class SkyblockEvent {
    var isActive = false
        private set

    protected abstract fun shouldStart(): Boolean

    protected abstract fun shouldEnd(): Boolean

    private val eventStartStop: BukkitRunnable =
        object : BukkitRunnable() {
            override fun run() {
                if (isActive && shouldEnd()) {
                    isActive = false
                } else if (!isActive && shouldStart()) {
                    isActive = true
                }
            }
        }

    fun track(skyblockPlugin: SkyblockPlugin) {
        eventStartStop.runTaskTimer(skyblockPlugin, 0L, 1L)
    }

    fun stopTracking() {
        if (!eventStartStop.isCancelled) {
            eventStartStop.cancel()
        }
    }
}

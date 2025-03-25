package firstplugin.skyblock.display

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.SkyblockPlayer
import firstplugin.skyblock.attributes.defense
import firstplugin.skyblock.attributes.intelligence
import firstplugin.skyblock.attributes.sbHealth
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.roundToInt

private const val UPDATE_ACTION_BAR_EVERY_TICKS = 5L

class ActionBarManager(
    private val plugin: SkyblockPlugin,
) {
    private var task: BukkitRunnable? = null

    fun initialize() {
        // Create a repeating task that runs every second (20 ticks)
        task =
            object : BukkitRunnable() {
                override fun run() {
                    updateAllActionBars()
                }
            }
        task?.runTaskTimer(plugin, 0L, UPDATE_ACTION_BAR_EVERY_TICKS)

        // Register a shutdown hook to cancel the task
        plugin.registerShutdownHook {
            task?.cancel()
        }
    }

    private fun updateAllActionBars() {
        // Get the SkyblockManager instance
        val skyblockManager = plugin.skyblockManager

        // Update action bar for all online players
        skyblockManager.onlinePlayers.forEach {
            updatePlayerActionBar(it)
        }
    }

    private fun updatePlayerActionBar(player: SkyblockPlayer) {
        // Build the action bar component
        val actionBar = Component.text()

        // Color of health becomes Gold if player has non-zero absorption
        val healthColor = if (player.hasAbsorption) NamedTextColor.GOLD else player.sbHealth.color

        // Add health to action bar
        actionBar.append(
            Component
                .text(
                    "${player.sbHealth.current.roundToInt() + player.sbHealth.absorption.roundToInt()}" +
                        "/${player.sbHealth.max.roundToInt()}",
                ).color(healthColor),
        )
        actionBar.append(
            Component
                .text("${player.sbHealth.symbol}     ")
                .color(healthColor),
        )

        // Add defense to action bar
        actionBar.append(
            Component
                .text("${player.defense.value.roundToInt()}")
                .color(player.defense.color),
        )
        actionBar.append(
            Component
                .text("${player.defense.symbol} Defense     ")
                .color(player.defense.color),
        )

        actionBar.append(
            Component
                .text(
                    "${player.intelligence.currentMana}/${player.intelligence.maxMana}",
                ).color(player.intelligence.color),
        )

        // Add mana to action bar
        actionBar.append(
            Component
                .text("${player.intelligence.symbol} Mana")
                .color(player.intelligence.color),
        )

        player.bukkitPlayer.sendActionBar(actionBar.build())
    }
}

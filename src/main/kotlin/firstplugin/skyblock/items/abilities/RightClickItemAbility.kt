package firstplugin.skyblock.items.abilities

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.entity.CombatEntity
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.entity.intelligence
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.scheduler.BukkitRunnable

interface RightClickItemAbility : ItemAbility {
    /**
     * Mana cost is a non-negative cost of mana incurred on the user
     * when the ability is used.
     */
    val manaCost: Int

    /**
     * Stored in minecraft ticks. So 20 ticks = 1 second. The cooldown must be **positive**.
     */
    val cooldown: Long

    val cooldownInSeconds: Int
        get() = (cooldown / 20).toInt()

    fun ability(plugin: SkyblockPlugin): Any

    var isOnCooldown: Boolean

    var timeLeftForCooldownToEnd: Int

    /**
     * Should be run once per second (or 20 ticks).
     */
    val cooldownTask: BukkitRunnable
        get() =
            object : BukkitRunnable() {
                override fun run() {
                    if (timeLeftForCooldownToEnd == 0) {
                        isOnCooldown = false
                        cancel()
                    }
                    timeLeftForCooldownToEnd--
                }
            }

    /**
     * Can and should be overridden whenever appropriate.
     */
    val cooldownMessage: Component
        get() =
            Component
                .text("This ability is on cooldown for ${timeLeftForCooldownToEnd}s.")
                .color(NamedTextColor.RED)

    fun activateAbility(
        plugin: SkyblockPlugin,
        holder: CombatEntity,
    ) {
        if (isOnCooldown) {
            (holder as? SkyblockPlayer)?.sendMessage(cooldownMessage)
        }
        if (holder.intelligence.current < manaCost) {
            holder.notEnoughMana = true
            return
        }
        holder.intelligence.current -= manaCost
        ability(plugin)
        isOnCooldown = true
        cooldownTask.runTaskTimer(plugin, 0L, 20L)
    }
}

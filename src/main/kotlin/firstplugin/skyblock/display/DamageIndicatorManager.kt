package firstplugin.skyblock.display

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.attributes.damage.DealtDamage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.scheduler.BukkitRunnable
import java.util.Random

private const val DAMAGE_INDICATOR_STAYS_IN_TICKS = 20L

class DamageIndicatorManager(
    private val plugin: SkyblockPlugin,
) {
    fun registerDamage(
        dealtDamage: DealtDamage,
        location: Location,
    ) {
        val coloredDamageIndication: Component =
            formatDamageIndication(dealtDamage.damageAmt.toInt().toDouble(), dealtDamage.critHit)
        val randomisedLocation = locationRandomizer(location)
        val armorStand =
            spawnNamedArmorStand(
                randomisedLocation,
                coloredDamageIndication,
            )
        val removeArmorStandLater =
            object : BukkitRunnable() {
                override fun run() {
                    armorStand.remove()
                }
            }.runTaskLater(plugin, DAMAGE_INDICATOR_STAYS_IN_TICKS)
    }

    private fun formatDamageIndication(
        damage: Double,
        critHit: Boolean,
    ): Component {
        if (!critHit) {
            return Component.text(damage).color(NamedTextColor.GRAY)
        }
        val colorsRepeat =
            listOf(
                NamedTextColor.WHITE,
                NamedTextColor.YELLOW,
                NamedTextColor.GOLD,
                NamedTextColor.RED,
                NamedTextColor.RED,
            )
        val rawDamageString = damage.toString()
        val totalComponent = Component.text()
        totalComponent.append(Component.text("✧").color(NamedTextColor.WHITE))
        var colorIndex = 0
        for (char in rawDamageString) {
            totalComponent.append(Component.text(char).color(colorsRepeat[colorIndex % 5]))
            colorIndex++
        }
        totalComponent.append(Component.text("✧").color(NamedTextColor.WHITE))
        return totalComponent.build()
    }

    private fun spawnNamedArmorStand(
        location: Location,
        name: Component,
    ): ArmorStand {
        val world = location.world
        val armorStand =
            world.spawn(location, ArmorStand::class.java) { armorStand ->
                armorStand.isVisible = false
                armorStand.isInvulnerable = true
                armorStand.setGravity(false)
                armorStand.canPickupItems = false

                armorStand.isCustomNameVisible = true
                armorStand.customName(name)

                armorStand.setBasePlate(false)
                armorStand.isMarker = true // No collision
            }
        return armorStand
    }

    private fun locationRandomizer(location: Location): Location {
        val rng = Random()
        var x = location.x
        var y = location.y
        var z = location.z
        x += (rng.nextDouble() - 0.5) / 2
        y += (rng.nextDouble() - 0.5) / 2
        z += (rng.nextDouble() - 0.5) / 2
        return Location(location.world, x, y, z)
    }
}

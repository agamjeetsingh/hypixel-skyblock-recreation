package firstplugin.skyblock.items.weapons.swords

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.Damage
import firstplugin.skyblock.attributes.staticAttributes.Speed
import firstplugin.skyblock.entity.CombatEntity
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.entity.speed
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.abilities.RightClickItemAbility
import firstplugin.skyblock.items.weapons.Sword
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable

class RogueSword(
    override val holder: ItemHolder,
) : Sword(Material.GOLDEN_SWORD) {
    // Initialize properties before setupItem()
    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "ROGUE_SWORD"
    override val itemName: String = "Rogue Sword"

    override val ability =
        object : RightClickItemAbility {
            override val manaCost: Int = 50

            override val cooldown: Long = 100L

            val speedBoost: Double = 100.0

            /**
             * In minecraft ticks.
             */
            val speedBoostForTime: Long = 30L * 20L

            override fun ability(plugin: SkyblockPlugin) {
                if (holder is CombatEntity) {
                    holder.speed.addEffect(ConstantAttributeEffect(speedBoost))
                }
                object : BukkitRunnable() {
                    override fun run() {
                        if (holder is CombatEntity) {
                            holder.speed.removeEffect(ConstantAttributeEffect(speedBoost))
                        }
                    }
                }.runTaskLater(plugin, speedBoostForTime)
            }

            override var isOnCooldown: Boolean = false

            override var timeLeftForCooldownToEnd: Int = 0

            override val title: Component =
                Component
                    .text("Speed Boost")
                    .color(NamedTextColor.GOLD)
                    .decoration(TextDecoration.ITALIC, false)

            override val description: MutableList<Component>
                get() {
                    val lore: MutableList<Component> = mutableListOf()
                    var component =
                        Component
                            .text("Grants ")
                            .color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false)

                    component =
                        component.append(
                            Component
                                .text("+100${Speed(holder).symbol} Speed ")
                                .color(NamedTextColor.WHITE)
                                .decoration(TextDecoration.ITALIC, false),
                        )

                    component =
                        component.append(
                            Component
                                .text("for ")
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        )

                    component =
                        component.append(
                            Component
                                .text("${speedBoostForTime / 20}s")
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.ITALIC, false),
                        )

                    component =
                        component.append(
                            Component
                                .text(".")
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        )

                    lore.add(component)
                    return lore
                }
        }

    init {
        // Set attributes
        attributes.getAttribute<Damage>()!!.addEffect(ConstantAttributeEffect(20.0))

        // Call setupItem() AFTER properties are initialized
        setupItem()
    }
}

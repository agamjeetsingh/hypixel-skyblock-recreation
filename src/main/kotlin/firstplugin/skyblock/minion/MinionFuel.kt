package firstplugin.skyblock.minion

import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.SkyblockItem
import firstplugin.skyblock.utils.Time
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

abstract class MinionFuel(
    material: Material,
) : SkyblockItem(material) {
    override val itemCategory: ItemCategory = ItemCategory.MINION_FUEL

    abstract val minionSpeedMultiplier: Double

    /**
     * Stored as in-game ticks. To convert from wall clock time to in-game ticks
     * use [Time].
     */
    abstract val time: Long?

    open val condition: () -> Boolean = { true }

    val description: Component
        get() {
            var component =
                Component
                    .text("Increases the speed of your minion by ")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .append(
                        Component
                            .text(((minionSpeedMultiplier - 1) * 100).toInt().toString() + "%")
                            .color(NamedTextColor.GREEN)
                            .decoration(TextDecoration.ITALIC, false),
                    )
            if (time != null) {
                component =
                    component
                        .append(
                            Component
                                .text(" for ${Time.ticksToString(time!!)}!")
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        )
            } else {
                component =
                    component
                        .append(
                            Component
                                .text(". Unlimited Duration!")
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        )
            }
            return component
        }
}

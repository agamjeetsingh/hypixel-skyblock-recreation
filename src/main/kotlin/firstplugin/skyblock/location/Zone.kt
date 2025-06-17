package firstplugin.skyblock.location

import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

abstract class Zone {
    abstract val name: String
    abstract val color: NamedTextColor
    abstract val questLinesWithoutBulletPoints: SkyblockLore

    private val questLines: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            for (component in skyblockLore) {
                skyblockLore.addLore(BULLET_POINT_COMPONENT.append(component))
            }
            return skyblockLore
        }

    val discoveryText: SkyblockLore
        get() {
            val lore = SkyblockLore()
            lore.addLore(newAreaDiscovered)
            lore.addLore(
                Component
                    .text("$ZONE_SYMBOL ")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .append(
                        Component
                            .text(name)
                            .color(color)
                            .decoration(TextDecoration.ITALIC, false),
                    ),
            )
            lore.addEmptyLine()
            lore.addLore(questLines)

            return lore
        }

    val nameComponent: Component
        get() =
            Component
                .text(name)
                .color(color)
                .decoration(TextDecoration.ITALIC, false)

    companion object {
        private const val ZONE_SYMBOL: String = "⏣"

        private const val BULLET_POINT: String = "■"

        private val BULLET_POINT_COMPONENT: Component =
            Component
                .text("$BULLET_POINT ")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)

        private val newAreaDiscovered: Component =
            Component
                .text("NEW AREA DISCOVERED!")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false)
    }
}

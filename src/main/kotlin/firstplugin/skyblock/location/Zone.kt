package firstplugin.skyblock.location

import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

abstract class Zone {
    abstract val name: String
    abstract val color: NamedTextColor
    val questLines: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            for (questLine in questLinesString) {
                skyblockLore.addLore(
                    Component
                        .text("$ZONE_SYMBOL $questLine")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false),
                )
            }
            return skyblockLore
        }
    abstract val questLinesString: List<String>

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

    companion object {
        private const val ZONE_SYMBOL: String = "⏣"

        @JvmStatic
        protected val BULLET_POINT: String = "■"

        private val newAreaDiscovered: Component =
            Component
                .text("NEW AREA DISCOVERED!")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false)
    }
}

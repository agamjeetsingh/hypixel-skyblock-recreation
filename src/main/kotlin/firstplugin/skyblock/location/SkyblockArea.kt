package firstplugin.skyblock.location

import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

/**
 * This class represents both [Zone] and [SkyblockLocation] which extend this class. It has the implementation for
 * the discovery text shown to the player when a new area is discovered.
 *
 * @property name The name displayed when the area is referenced. E.g. in discovery texts
 * @property color The color of the name displayed when the area is referenced.
 * @property questLinesWithoutBulletPoints The quest lines but without bullet points. Bullet points are added by
 * the getter for [questLines].
 * @property questLines The quest lines with bullet points. These are directly used in [discoveryText]
 * @property discoveryText This [SkyblockLore] is ready to be used to be sent as messages or as item lore.
 * @property nameComponent This [Component] is used to reference this area. It has the correct name and color and is
 * not italic.
 */
abstract class SkyblockArea {
    abstract val name: String
    abstract val color: NamedTextColor
    abstract val questLinesWithoutBulletPoints: SkyblockLore

    private val questLines: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            for (component in questLinesWithoutBulletPoints) {
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

package firstplugin.skyblock.location.hub

import firstplugin.skyblock.location.Zone
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class Farm : Zone() {
    override val name: String = "Farm"
    override val color: NamedTextColor = NamedTextColor.AQUA
    override val questLinesWithoutBulletPoints: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            skyblockLore.addLore(
                Component
                    .text("Talk to the Farmer.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text("Gather Wheat.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text("Travel to ")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false)
                    .append(TheBarn().nameComponent)
                    .append(
                        Component
                            .text(".")
                            .color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false),
                    ),
            )
            return skyblockLore
        }
}

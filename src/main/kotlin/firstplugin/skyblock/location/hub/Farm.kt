package firstplugin.skyblock.location.hub

import firstplugin.skyblock.location.Zone
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class Farm : Zone() {
    override val name: String = "Farm"
    override val color: NamedTextColor = NamedTextColor.AQUA
    override val questLines: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            skyblockLore.addLore(
                Component
                    .text("$BULLET_POINT Talk to the Farmer.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text("$BULLET_POINT Gather Wheat.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text(),
            )
        }
    override val questLinesString: List<String> =
        listOf(
            "$BULLET_POINT Talk to the Farmer.",
            "$BULLET_POINT Gather Wheat.",
            "Travel to The Barn.", // TODO: Replace The Barn with the actual class
        )
}

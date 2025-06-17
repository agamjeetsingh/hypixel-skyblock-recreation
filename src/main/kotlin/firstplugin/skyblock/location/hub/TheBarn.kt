package firstplugin.skyblock.location.hub

import firstplugin.skyblock.location.Zone
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class TheBarn : Zone() {
    override val name: String = "The Barn"
    override val color: NamedTextColor = NamedTextColor.AQUA
    override val questLines: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            skyblockLore.addLore(
                Component
                    .text("$BULLET_POINT Harvest Wheat, Carrots, Potatoes, Pumpkins, and Melons.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text("$BULLET_POINT Kill cows, chickens, and pigs.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text("$BULLET_POINT Milk cows.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            return skyblockLore
        }
}

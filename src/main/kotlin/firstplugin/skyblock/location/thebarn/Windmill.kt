package firstplugin.skyblock.location.thebarn

import firstplugin.skyblock.location.SkyblockLocation
import firstplugin.skyblock.location.Zone
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

object Windmill : Zone() {
    override val location: SkyblockLocation = TheBarn
    override val name: String = "Windmill"
    override val color: NamedTextColor = NamedTextColor.AQUA
    override val questLinesWithoutBulletPoints: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            skyblockLore.addLore(
                Component
                    .text("Talk to the Windmill Operator.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            return skyblockLore
        }
}

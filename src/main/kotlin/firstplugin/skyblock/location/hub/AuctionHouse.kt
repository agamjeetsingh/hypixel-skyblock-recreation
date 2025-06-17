package firstplugin.skyblock.location.hub

import firstplugin.skyblock.location.SkyblockLocation
import firstplugin.skyblock.location.Zone
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class AuctionHouse : Zone() {
    override val name: String = "Auction House"
    override val color: NamedTextColor = NamedTextColor.GOLD
    override val questLinesWithoutBulletPoints: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            skyblockLore.addLore(
                Component
                    .text("Auction off your special items.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text("Bid on other players' items.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            return skyblockLore
        }
    override val location: SkyblockLocation = Hub()
}

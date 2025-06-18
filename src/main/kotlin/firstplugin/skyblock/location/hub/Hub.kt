package firstplugin.skyblock.location.hub

import firstplugin.skyblock.location.SkyblockLocation
import firstplugin.skyblock.location.Zone
import firstplugin.skyblock.skill.Skill
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.format.NamedTextColor

object Hub : SkyblockLocation() {
    override val skillRequirements: List<Skill.Requirement<Skill>> = listOf()
    override val zones: List<Zone> = listOf(AuctionHouse, Bank, Farm)
    override val name: String = "Hub"
    override val color: NamedTextColor = NamedTextColor.WHITE
    override val questLinesWithoutBulletPoints: SkyblockLore = SkyblockLore()
}

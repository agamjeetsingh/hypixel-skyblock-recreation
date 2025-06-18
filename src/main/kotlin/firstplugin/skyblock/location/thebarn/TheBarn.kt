package firstplugin.skyblock.location.thebarn

import firstplugin.skyblock.location.SkyblockLocation
import firstplugin.skyblock.location.Zone
import firstplugin.skyblock.skill.FarmingSkill
import firstplugin.skyblock.skill.Skill
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

object TheBarn : SkyblockLocation() {
    override val name: String = "The Barn"
    override val color: NamedTextColor = NamedTextColor.AQUA
    override val questLinesWithoutBulletPoints: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            skyblockLore.addLore(
                Component
                    .text("Harvest Wheat, Carrots, Potatoes, Pumpkins, and Melons.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text("Kill cows, chickens, and pigs.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(
                Component
                    .text("Milk cows.")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
            return skyblockLore
        }
    override val skillRequirements: List<Skill.Requirement<Skill>> =
        listOf(
            Skill.Requirement(FarmingSkill::class, 1),
        )

    override val zones: List<Zone> = listOf()
}

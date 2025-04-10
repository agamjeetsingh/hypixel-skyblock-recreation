@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.skill

import firstplugin.skyblock.attributes.*
import firstplugin.skyblock.attributes.staticAttributes.CritChance
import firstplugin.skyblock.attributes.staticAttributes.Damage
import firstplugin.skyblock.entity.SkyblockPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

class CombatSkill(
    player: SkyblockPlayer,
) : Skill(player) {
    override val skillName: String = "Combat"

    override val specialSkillName: String = "Warrior"

    override fun primarySkillRewardMessage(): List<TextComponent> {
        var component = Component.text("")
        component =
            component.append(
                Component
                    .text("   Deal ")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false),
            )
        val oldDamageBonus = if (level <= 50) level * 4 else (150 + (level - 50))
        val newDamageBonus = oldDamageBonus + (if (level <= 50) 4 else 1)

        // No oldDamageBonus to display when upgrading from level 0 to 1
        if (level > 0) {
            component =
                component
                    .append(
                        Component
                            .text(oldDamageBonus)
                            .color(NamedTextColor.DARK_GRAY)
                            .decoration(TextDecoration.ITALIC, false),
                    ).append(
                        Component
                            .text(SKILL_LEVEL_UP_ARROW)
                            .color(NamedTextColor.DARK_GRAY)
                            .decoration(TextDecoration.BOLD, true)
                            .decoration(TextDecoration.ITALIC, false),
                    )
        }

        component =
            component
                .append(
                    Component
                        .text("$newDamageBonus%")
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(" more damage to mobs.")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false),
                )
        return listOf(component)
    }

    override val primaryStat: Attribute = player.attributes.getAttribute<Damage>()!!
    override val secondaryStat: Attribute = player.attributes.getAttribute<CritChance>()!!

    override val primaryStatReward: AttributeEffect = ConstantAttributeEffect(0.005)
    override val secondaryStatReward: AttributeEffect =
        if (level <= 50) {
            AdditiveAttributeEffect(0.04)
        } else {
            AdditiveAttributeEffect(0.01)
        }

    override val icon: Material = Material.STONE_SWORD
}

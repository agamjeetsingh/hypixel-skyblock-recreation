package firstplugin.skyblock.skill

import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.AttributeEffect
import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.attributes.dynamicAttributes.Health
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.FarmingFortune
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.utils.StringUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

class FarmingSkill(
    player: SkyblockPlayer,
) : Skill(player) {
    override val skillName: String = "Farming"

    override val specialSkillName: String = "Farmhand"

    override val primaryStat: Attribute = player.attributes.getAttribute<FarmingFortune>()!!
    override val secondaryStat: Attribute = player.attributes.getAttribute<Health>()!!

    override val primaryStatReward: AttributeEffect = ConstantAttributeEffect(4.0)

    override val secondaryStatReward: AttributeEffect
        get() =
            when {
                level <= 14 -> ConstantAttributeEffect(2.0)
                level <= 19 -> ConstantAttributeEffect(3.0)
                level <= 25 -> ConstantAttributeEffect(4.0)
                level <= 60 -> ConstantAttributeEffect(5.0)
                else -> ConstantAttributeEffect(0.0)
            }

    override val icon: Material = Material.GOLDEN_HOE

    override fun primarySkillRewardMessage(): List<TextComponent> {
        val finalMessage: MutableList<TextComponent> = mutableListOf()

        var component = Component.text("")
        component =
            component
                .append(
                    Component
                        .text("Grants ")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text("+")
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                )

        val oldFarmingFortuneBonus = (level - 1) * 4
        val newFarmingFortuneBonus = level * 4

        if (level > 0) {
            component =
                component
                    .append(
                        Component
                            .text(oldFarmingFortuneBonus)
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

        val farmingFortuneInstance = FarmingFortune()
        val spacedName = StringUtils.camelCaseToSpaced(farmingFortuneInstance::class.simpleName!!)

        component =
            component
                .append(
                    Component
                        .text("$newFarmingFortuneBonus")
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(" ${farmingFortuneInstance.symbol} $spacedName")
                        .color(farmingFortuneInstance.color)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(",")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false),
                )
        finalMessage.add(component)

        component = Component.text("")
        component =
            component
                .append(
                    Component
                        .text("   which increases your chance for multiple crops.")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false),
                )
        finalMessage.add(component)

        return finalMessage
    }
}

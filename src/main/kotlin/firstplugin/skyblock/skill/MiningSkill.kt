package firstplugin.skyblock.skill

import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.AttributeEffect
import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.Defense
import firstplugin.skyblock.attributes.staticAttributes.MiningFortune
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.utils.StringUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

class MiningSkill(
    player: SkyblockPlayer,
) : Skill(player) {
    override val skillName: String = "Mining"

    override val specialSkillName: String = "Spelunker"

    override val primaryStat: Attribute = player.attributes.getAttribute<MiningFortune>()!!

    override val secondaryStat: Attribute = player.attributes.getAttribute<Defense>()!!

    override val primaryStatReward: AttributeEffect = ConstantAttributeEffect(4.0)

    override val secondaryStatReward: AttributeEffect
        get() {
            return when {
                level <= 14 -> ConstantAttributeEffect(1.0)
                level <= 60 -> ConstantAttributeEffect(2.0)
                else -> ConstantAttributeEffect(0.0)
            }
        }
    override val icon: Material = Material.STONE_PICKAXE

    override fun primarySkillRewardMessage(): List<TextComponent>? {
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

        val oldMiningFortuneBonus = (level - 1) * 4
        val newMiningFortuneBonus = level * 4

        if (level > 0) {
            component =
                component
                    .append(
                        Component
                            .text(oldMiningFortuneBonus)
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

        val miningFortuneInstance = MiningFortune()
        val spacedName = StringUtils.camelCaseToSpaced(miningFortuneInstance::class.simpleName!!)

        component =
            component
                .append(
                    Component
                        .text("$newMiningFortuneBonus")
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(" ${miningFortuneInstance.symbol} $spacedName")
                        .color(miningFortuneInstance.color)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(",")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false),
                )
        finalMessage.add(component)

        component =
            Component
                .text("   which increases your chance for multiple ores.")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)

        finalMessage.add(component)

        // TODO

        return finalMessage
    }
}

package firstplugin.skyblock.skill

import firstplugin.skyblock.utils.NumberFormat
import firstplugin.skyblock.utils.RomanNumerals
import firstplugin.skyblock.utils.SkyblockLore
import firstplugin.skyblock.utils.StringUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class SkillRewardMessage(
    private val skill: Skill,
) {
    /**
     * Creates a formatted message to display when a player levels up a skill.
     * The message includes:
     * - A decorative border
     * - The skill name and level change
     * - A list of rewards
     * - Another decorative border
     *
     * @return A list of TextComponent objects representing each line of the message
     */
    fun congratulationsMessage(): SkyblockLore {
        val skyblockLore = SkyblockLore()
        // Top border
        skyblockLore.addLore(
            Component
                .text(CHAT_BORDER_SYMBOL.repeat(MAX_BORDERS_IN_A_SINGLE_LINE))
                .color(NamedTextColor.DARK_AQUA)
                .decoration(TextDecoration.ITALIC, false),
        )

        // Skill level up header
        var component =
            Component
                .text("")
                .append(
                    Component
                        .text(" SKILL LEVEL UP ")
                        .color(NamedTextColor.AQUA)
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(skill.skillName)
                        .color(NamedTextColor.DARK_AQUA)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(" ${RomanNumerals.toRoman(skill.level - 1)}")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(SKILL_LEVEL_UP_ARROW)
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false),
                ).append(
                    Component
                        .text(RomanNumerals.toRoman(skill.level))
                        .color(NamedTextColor.DARK_AQUA)
                        .decoration(TextDecoration.ITALIC, false),
                )

        skyblockLore.addLore(component, newIndent = 1)

        skyblockLore.addEmptyLine()

        // Rewards header
        component =
            Component
                .text("REWARDS")
                .color(NamedTextColor.GREEN)
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false)
        skyblockLore.addLore(component, newIndent = 1)

        // Special skill name and level
        component =
            Component
                .text("  ${skill.specialSkillName} ${RomanNumerals.toRoman(skill.level)}")
                .color(NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false)
        skyblockLore.addLore(component, newIndent = 1)

        // Add all reward messages
        skyblockLore.addLore(rewardMessage, maintainIndent = true)

        // Bottom border
        skyblockLore.addLore(
            Component
                .text(CHAT_BORDER_SYMBOL.repeat(MAX_BORDERS_IN_A_SINGLE_LINE))
                .color(NamedTextColor.DARK_AQUA)
                .decoration(TextDecoration.ITALIC, false),
        )

        return skyblockLore
    }

    private fun coinRewardMessage(): TextComponent =
        Component
            .text("+")
            .color(NamedTextColor.DARK_GRAY)
            .decoration(TextDecoration.ITALIC, false)
            .append(
                Component
                    .text(NumberFormat.formatWithCommas(skill.coinReward[skill.level]!!))
                    .color(NamedTextColor.GOLD)
                    .decoration(TextDecoration.ITALIC, false),
            ).append(
                Component
                    .text(" Coins")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
            )

    private fun secondarySkillRewardMessage(): TextComponent {
        val secondaryStatClassName = skill.secondaryStat::class.simpleName!!
        val spacedName = StringUtils.camelCaseToSpaced(secondaryStatClassName)

        val clazz = skill.secondaryStat::class.java
        // Just needed for secondaryStatNewInstance.prettyPrintValueForMenu
        val secondaryStatNewInstance = clazz.getDeclaredConstructor().newInstance()
        secondaryStatNewInstance.addEffect(skill.secondaryStatReward)

        return Component
            .text("+")
            .color(NamedTextColor.DARK_GRAY)
            .decoration(TextDecoration.ITALIC, false)
            .append(
                Component
                    .text("${secondaryStatNewInstance.prettyPrintValueForMenu} ")
                    .color(NamedTextColor.GREEN)
                    .decoration(TextDecoration.ITALIC, false),
            ).append(
                Component
                    .text("${skill.secondaryStat.symbol} ")
                    .color(skill.secondaryStat.color)
                    .decoration(TextDecoration.ITALIC, false),
            ).append(
                Component
                    .text(" $spacedName")
                    .color(skill.secondaryStat.color)
                    .decoration(TextDecoration.ITALIC, false),
            )
    }

    /**
     * List of text components describing rewards gained when leveling up
     */
    private val rewardMessage: List<Component>
        get() {
            val finalMessage: MutableList<Component> = mutableListOf()

            skill.primarySkillRewardMessage()?.let { finalMessage.addAll(it) }
                ?: finalMessage.add(skill.alternativePrimaryRewardMessage()!!)

            finalMessage.add(secondarySkillRewardMessage())

            finalMessage.addAll(skill.otherRewardsMessage())

            finalMessage.add(coinRewardMessage())

            finalMessage.add(skill.skyblockLevelXPReward.rewardMessage)

            return finalMessage
        }
}

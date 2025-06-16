package firstplugin.skyblock.skill

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.rewards.SkyblockReward
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class SkillXPReward(
    private val skill: Skill,
    xp: Int,
) : SkyblockReward {
    private val reward = 0.coerceAtLeast(xp)

    override val rewardMessage: Component =
        Component
            .text("+")
            .color(NamedTextColor.DARK_GRAY)
            .decoration(TextDecoration.ITALIC, false)
            .append(
                Component
                    .text(reward)
                    .color(NamedTextColor.DARK_AQUA)
                    .decoration(TextDecoration.ITALIC, false),
            ).append(
                Component
                    .text(" ${skill.skillName} Experience")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
            )

    override fun applyRewardTo(
        skyblockPlayer: SkyblockPlayer,
        skyblockPlugin: SkyblockPlugin?,
    ) {
        skyblockPlayer.skills.find { skill::class == it::class }?.gainXP(reward.toDouble(), skyblockPlugin ?: return)
    }
}

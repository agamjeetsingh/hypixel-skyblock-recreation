package firstplugin.skyblock.xp

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.rewards.SkyblockReward
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class SkyblockXPReward(
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
                    .text("$reward Skyblock XP")
                    .color(NamedTextColor.AQUA)
                    .decoration(TextDecoration.ITALIC, false),
            )

    override fun applyRewardTo(
        skyblockPlayer: SkyblockPlayer,
        skyblockPlugin: SkyblockPlugin?,
    ) {
        skyblockPlayer.skyblockLevel.gainXP(reward)
    }
}

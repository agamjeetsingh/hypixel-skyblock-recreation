package firstplugin.skyblock.rewards

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.entity.SkyblockPlayer
import net.kyori.adventure.text.Component

interface SkyblockReward {
    val rewardMessage: Component

    fun applyRewardTo(
        skyblockPlayer: SkyblockPlayer,
        // TODO - Make non nullable in future
        skyblockPlugin: SkyblockPlugin? = null,
    )
}

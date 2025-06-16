package firstplugin.skyblock.items.enchantment

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.rewards.SkyblockReward
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class EnchantmentXPDiscountReward(
    private val enchantment: Enchantment,
    /**
     * Example: 25 for -25%
     */
    private val discountPercentageReduction: Int,
) : SkyblockReward {
    override val rewardMessage: Component =
        Component
            .text("${enchantment.name} ")
            .color(enchantment.rarity.color)
            .decoration(TextDecoration.ITALIC, false)
            .append(
                Component
                    .text("Exp Discount ")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
            ).append(
                Component
                    .text("(-$discountPercentageReduction%)")
                    .color(NamedTextColor.GREEN)
                    .decoration(TextDecoration.ITALIC, false),
            )

    override fun applyRewardTo(
        skyblockPlayer: SkyblockPlayer,
        skyblockPlugin: SkyblockPlugin?,
    ) {
        TODO("Not yet implemented")
    }
}

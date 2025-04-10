package firstplugin.skyblock.items.enchantment

import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.SkyblockItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

abstract class Enchantment(
    val tier: EnchantmentTier,
) {
    open val minTier: EnchantmentTier = EnchantmentTier.I
    open val maxTier: EnchantmentTier = EnchantmentTier.I

    val rarity: Rarity = tier.rarity
    open val conflictsWith: List<Enchantment> = listOf()
    abstract val applicableOn: List<ItemCategory>
    abstract val enchantmentType: EnchantmentType
    abstract val name: String
    abstract val description: MutableList<Component>

    fun <T> applyEnchantment(item: T) where T : SkyblockItem, T : Enchantable {
        TODO()
    }

    fun formattedForLore(): Component =
        if (enchantmentType == EnchantmentType.NORMAL) {
            Component
                .text(name + tier.name)
                .color(NamedTextColor.BLUE)
                .decoration(TextDecoration.ITALIC, false)
        } else {
            Component
                .text(name + tier.name)
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true)
        }
}

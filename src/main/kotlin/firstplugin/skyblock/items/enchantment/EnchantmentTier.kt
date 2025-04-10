package firstplugin.skyblock.items.enchantment

import firstplugin.skyblock.items.Rarity

enum class EnchantmentTier(
    val rarity: Rarity,
) {
    I(Rarity.COMMON),
    II(Rarity.COMMON),
    III(Rarity.COMMON),
    IV(Rarity.COMMON),
    V(Rarity.UNCOMMON),
    VI(Rarity.RARE),
    VII(Rarity.EPIC),
    VIII(Rarity.LEGENDARY),
    IX(Rarity.MYTHIC),
    X(Rarity.MYTHIC),
}

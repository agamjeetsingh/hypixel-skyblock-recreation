package firstplugin.skyblock.items.enchantment

enum class EnchantmentType {
    /**
     * An item can have an unlimited amount of Normal Enchantments on it,
     * assuming none of them conflict with one another.
     */
    NORMAL,

    /**
     * An item can only have one Ultimate Enchantment applied at once.
     */
    ULTIMATE,
}

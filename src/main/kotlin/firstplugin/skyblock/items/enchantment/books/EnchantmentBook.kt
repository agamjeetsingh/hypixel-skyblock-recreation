package firstplugin.skyblock.items.enchantment.books

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.SkyblockItem
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

class EnchantmentBook(
    override val holder: ItemHolder,
) : SkyblockItem(Material.ENCHANTED_BOOK) {
    override val itemRarity: Rarity = Rarity.COMMON // Default rarity, can be overridden
    override val itemCategory: ItemCategory = ItemCategory.ENCHANTMENT_BOOK

    override fun loreDescription(): SkyblockLore {
        val skyblockLore = SkyblockLore()
        skyblockLore.addLore(
            Component
                .text("Enchantment Book")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false),
        )
        return skyblockLore
    }
}

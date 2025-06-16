package firstplugin.skyblock.items.vanilla

import firstplugin.skyblock.collection.Collection
import firstplugin.skyblock.collection.CollectionItem
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.SkyblockItem
import org.bukkit.Material

class Cobblestone(
    override val holder: ItemHolder,
) : SkyblockItem(Material.COBBLESTONE),
    CollectionItem {
    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "COBBLESTONE"
    override val itemCategory: ItemCategory = ItemCategory.BLOCK
    override val collection: Collection
        get() = TODO("Not yet implemented")
}
package firstplugin.skyblock.items.ores

import firstplugin.skyblock.collection.CollectionItem
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.SkyblockItem
import org.bukkit.Material

class Diamond(
    override val holder: ItemHolder,
) : SkyblockItem(Material.DIAMOND),
    CollectionItem {
    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "DIAMOND"

    override val itemCategory: ItemCategory = ItemCategory.ORE

    init {
        setupItem()
    }
}

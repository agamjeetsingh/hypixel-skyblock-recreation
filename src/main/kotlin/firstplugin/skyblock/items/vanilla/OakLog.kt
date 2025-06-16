package firstplugin.skyblock.items.vanilla

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.SkyblockItem
import org.bukkit.Material

class OakLog(
    override val holder: ItemHolder,
) : SkyblockItem(Material.OAK_LOG) {
    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "LOG"
    override val itemCategory: ItemCategory? = null

    init {
        setupItem()
    }
}

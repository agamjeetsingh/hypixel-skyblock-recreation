package firstplugin.skyblock.mining.pickaxe

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import org.bukkit.Material

class WoodenPickaxe(
    override val holder: ItemHolder,
) : Pickaxe(Material.WOODEN_PICKAXE) {
    override val damage: Double = 15.0

    override val miningSpeed: Double = 70.0

    override val breakingPower: Int = 1

    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "WOOD_PICKAXE"

    init {
        setupItem()
    }
}

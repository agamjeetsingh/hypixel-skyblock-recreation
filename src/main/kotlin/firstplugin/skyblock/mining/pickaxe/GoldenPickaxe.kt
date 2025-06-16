package firstplugin.skyblock.mining.pickaxe

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import org.bukkit.Material

class GoldenPickaxe(
    override val holder: ItemHolder,
) : Pickaxe(Material.GOLDEN_PICKAXE) {
    override val breakingPower: Int = 1

    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "GOLD_PICKAXE"

    override val damage: Double = 15.0

    override val miningSpeed: Double = 250.0

    init {
        setupItem()
    }
}

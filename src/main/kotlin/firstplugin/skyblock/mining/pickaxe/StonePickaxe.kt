package firstplugin.skyblock.mining.pickaxe

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import org.bukkit.Material

class StonePickaxe(
    override val holder: ItemHolder,
) : Pickaxe(Material.STONE_PICKAXE) {
    override val damage: Double = 25.0

    override val miningSpeed: Double = 110.0

    override val breakingPower: Int = 2

    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "STONE_PICKAXE"

    init {
        setupItem()
    }
}

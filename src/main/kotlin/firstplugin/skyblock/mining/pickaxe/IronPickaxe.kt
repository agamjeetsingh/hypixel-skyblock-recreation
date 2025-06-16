package firstplugin.skyblock.mining.pickaxe

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import org.bukkit.Material

class IronPickaxe(
    override val holder: ItemHolder,
) : Pickaxe(Material.IRON_PICKAXE) {
    override var damage: Double = 25.0

    override var miningSpeed: Double = 160.0

    override val breakingPower: Int = 3

    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "IRON_PICKAXE"

    init {
        setupItem()
    }
}

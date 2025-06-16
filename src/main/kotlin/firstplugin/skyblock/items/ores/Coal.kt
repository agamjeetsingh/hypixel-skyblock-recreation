package firstplugin.skyblock.items.ores

import firstplugin.skyblock.collection.CollectionItem
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.market.bazaar.BazaarSellable
import firstplugin.skyblock.market.trade.Tradable
import firstplugin.skyblock.minion.MinionFuel
import firstplugin.skyblock.utils.Time
import org.bukkit.Material

class Coal(
    override val holder: ItemHolder,
) : MinionFuel(Material.COAL),
    CollectionItem,
    Tradable,
    BazaarSellable {
    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "COAL"

    override val minionSpeedMultiplier: Double = 1.05

    override val time: Long = Time.minutesToTicks(30)

    init {
        setupItem()
    }
}

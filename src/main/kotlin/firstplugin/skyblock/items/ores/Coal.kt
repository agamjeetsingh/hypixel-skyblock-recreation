package firstplugin.skyblock.items.ores

import firstplugin.skyblock.bazaar.BazaarSellable
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.minion.MinionFuel
import firstplugin.skyblock.utils.SkyblockLore
import firstplugin.skyblock.utils.Time
import firstplugin.trade.Tradable
import org.bukkit.Material

class Coal(
    override val holder: ItemHolder,
) : MinionFuel(Material.COAL),
    Tradable,
    BazaarSellable {
    override val itemRarity: Rarity = Rarity.COMMON

    override fun loreDescription(): SkyblockLore {
        TODO("Not yet implemented")
    }

    override val minionSpeedMultiplier: Double = 1.05

    override val time: Long = Time.minutesToTicks(30)
}

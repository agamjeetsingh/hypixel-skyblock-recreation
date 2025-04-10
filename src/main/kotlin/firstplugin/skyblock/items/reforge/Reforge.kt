package firstplugin.skyblock.items.reforge

import firstplugin.skyblock.attributes.AttributeEffect
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity

interface Reforge {
    // Change from ItemCategory to something like Sword, Bows, Armor etc. TODO
    var reforgePrefix: String

    val appliesTo: List<ItemCategory>

    val effects: List<AttributeEffect>

    // Change from Double to something like Int or Coin TODO

    companion object {
        val reforgeCost: Map<Rarity, Double> =
            mapOf(
                Rarity.COMMON to 250.0,
                Rarity.UNCOMMON to 500.0,
                Rarity.RARE to 1000.0,
                Rarity.EPIC to 2500.0,
                Rarity.LEGENDARY to 5000.0,
                Rarity.MYTHIC to 10000.0,
                Rarity.DIVINE to 10000.0,
                Rarity.SPECIAL to 25000.0,
                Rarity.VERY_SPECIAL to 25000.0,
            )

        fun randomReforge(): Reforge {
            TODO()
        }
    }
}

package firstplugin.skyblock.items.weapons.swords

import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.Damage
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.weapons.Sword
import org.bukkit.Material

class GoldenSword(
    override val holder: ItemHolder,
) : Sword(Material.GOLDEN_SWORD) {
    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "GOLD_SWORD"

    init {
        attributes.getAttribute<Damage>()!!.addEffect(ConstantAttributeEffect(20.0))

        setupItem()
    }
}

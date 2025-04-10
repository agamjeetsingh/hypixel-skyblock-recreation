package firstplugin.skyblock.items.weapons.swords

import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.Damage
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.weapons.Sword
import org.bukkit.Material

class DiamondSword(
    override val holder: ItemHolder,
) : Sword(Material.DIAMOND_SWORD) {
    override val itemRarity: Rarity = Rarity.UNCOMMON

    init {
        attributes.getAttribute<Damage>()!!.addEffect(ConstantAttributeEffect(35.0))

        setupItem()
    }
}

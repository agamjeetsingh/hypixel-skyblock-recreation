package firstplugin.skyblock.items.weapons.swords

import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.Damage
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.weapons.Sword
import org.bukkit.Material

class IronSword(
    override val holder: ItemHolder,
) : Sword(Material.IRON_SWORD) {
    override val itemRarity: Rarity = Rarity.COMMON

    init {
        attributes.getAttribute<Damage>()!!.addEffect(ConstantAttributeEffect(30.0))

        setupItem()
    }
}

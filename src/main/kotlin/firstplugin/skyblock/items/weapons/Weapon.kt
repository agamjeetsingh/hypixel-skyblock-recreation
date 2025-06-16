@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.items.weapons

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.Attribute.Companion.setupDefaultItemAttributes
import firstplugin.skyblock.attributes.staticAttributes.*
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.SkyblockItem
import firstplugin.skyblock.items.abilities.AbilityHolder
import firstplugin.skyblock.items.abilities.ItemAbility
import firstplugin.skyblock.items.enchantment.Enchantable
import firstplugin.skyblock.items.enchantment.EnchantableDelegate
import firstplugin.skyblock.items.generateAttributeLore
import firstplugin.skyblock.items.reforge.Reforge
import firstplugin.skyblock.items.reforge.Reforgeable
import firstplugin.skyblock.utils.SkyblockLore
import org.bukkit.Material

sealed class Weapon(
    material: Material,
) : SkyblockItem(material),
    Reforgeable,
    Attributable,
    Enchantable by EnchantableDelegate(),
    AbilityHolder {
    override val itemCategory: ItemCategory = ItemCategory.WEAPON

    override val attributes: List<Attribute> by lazy {
        setupDefaultItemAttributes(holder)
    }

    override var reforge: Reforge? = null

    override val ability: ItemAbility? = null

    private val showAttributes: List<Class<out Attribute>> =
        listOf(
            Damage::class.java,
            Strength::class.java,
            CritChance::class.java,
            CritDamage::class.java,
            BonusAttackSpeed::class.java,
        )

    override fun loreDescription(): SkyblockLore {
        val skyblockLore = SkyblockLore()

        // Show attribute modifiers

        skyblockLore.addLore(this.generateAttributeLore(showAttributes))

        // Add enchantments

        val enchantmentLore = enchantmentLore()
        if (enchantmentLore.isNotEmpty()) {
            skyblockLore.addEmptyLine()
            skyblockLore.addLore(enchantmentLore)
        }

        // Add ability, if it exists

        if (ability != null) {
            val abilityLore = ability!!.abilityLore
            if (abilityLore.isNotEmpty()) {
                skyblockLore.addEmptyLine()
                skyblockLore.addLore(abilityLore)
            }
        }

        return skyblockLore
    }
}

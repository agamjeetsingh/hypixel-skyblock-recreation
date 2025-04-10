@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.items.weapons

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.Attribute.Companion.setupDefaultItemAttributes
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.*
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.SkyblockItem
import firstplugin.skyblock.items.abilities.AbilityHolder
import firstplugin.skyblock.items.abilities.ItemAbility
import firstplugin.skyblock.items.enchantment.Enchantable
import firstplugin.skyblock.items.enchantment.EnchantableDelegate
import firstplugin.skyblock.items.reforge.Reforge
import firstplugin.skyblock.items.reforge.Reforgeable
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
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

    override fun loreDescription(): SkyblockLore {
        val skyblockLore = SkyblockLore()

        // Show attribute modifiers

        if (attributes.getAttribute<Damage>()!!.isNotDefault) {
            val damageComponent: Component =
                Component
                    .text("Damage: ")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .append(
                        Component
                            .text(
                                "+" +
                                    attributes
                                        .getAttribute<Damage>()!!
                                        .value
                                        .toInt()
                                        .toString(),
                            ).color(NamedTextColor.RED)
                            .decoration(TextDecoration.ITALIC, false),
                    )
            skyblockLore.addLore(damageComponent)
        }

        if (attributes.getAttribute<Strength>()!!.isNotDefault) {
            val strengthComponent: Component =
                Component
                    .text("Strength: ")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
            strengthComponent.append(
                Component
                    .text(attributes.getAttribute<Strength>()!!.value)
                    .color(NamedTextColor.RED)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(strengthComponent)
        }

        if (attributes.getAttribute<CritChance>()!!.isNotDefault) {
            val critChanceComponent: Component =
                Component
                    .text("Crit Chance: ")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
            critChanceComponent.append(
                Component
                    .text((attributes.getAttribute<CritChance>()!!.value * 100).toInt().toString() + "%")
                    .color(NamedTextColor.RED)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(critChanceComponent)
        }

        if (attributes.getAttribute<CritDamage>()!!.isNotDefault) {
            val critDamageComponent: Component =
                Component
                    .text("Crit Damage: ")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
            critDamageComponent.append(
                Component
                    .text((attributes.getAttribute<CritDamage>()!!.value * 100).toInt().toString() + "%")
                    .color(NamedTextColor.RED)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(critDamageComponent)
        }

        if (attributes.getAttribute<BonusAttackSpeed>()!!.isNotDefault) {
            val bonusAttackSpeedComponent: Component =
                Component
                    .text("Bonus Attack Speed: ")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
            bonusAttackSpeedComponent.append(
                Component
                    .text((attributes.getAttribute<BonusAttackSpeed>()!!.value * 100).toInt().toString() + "%")
                    .color(NamedTextColor.RED)
                    .decoration(TextDecoration.ITALIC, false),
            )
            skyblockLore.addLore(bonusAttackSpeedComponent)
        }

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

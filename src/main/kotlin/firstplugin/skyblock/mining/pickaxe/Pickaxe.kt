package firstplugin.skyblock.mining.pickaxe

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.Attribute.Companion.setupDefaultItemAttributes
import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.Damage
import firstplugin.skyblock.attributes.staticAttributes.MiningSpeed
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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

abstract class Pickaxe(
    material: Material,
) : SkyblockItem(material),
    Attributable,
    Reforgeable,
    AbilityHolder,
    Enchantable by EnchantableDelegate() {
    override val itemCategory: ItemCategory? = ItemCategory.PICKAXE
    override var reforge: Reforge? = null
    override val attributes: List<Attribute> by lazy {
        setupDefaultItemAttributes(holder)
    }

    private val showAttributes: List<Class<out Attribute>> =
        listOf(
            Damage::class.java,
            MiningSpeed::class.java,
        )

    /**
     * Represents the base `damage` and `miningSpeed` provided by the pickaxe.
     * This does not include any bonuses (like +10 miningSpeed for every 100 blocks mined).
     * Such bonuses should be provided like this:
     * ```
     * attributes.getAttribute<MiningSpeed>()!!.addEffect(ConstantAttributeEffect(bonus))
     * ```
     */
    abstract val damage: Double
    abstract val miningSpeed: Double

    override fun setupItem() {
        attributes.getAttribute<Damage>()!!.addEffect(ConstantAttributeEffect(damage))
        attributes.getAttribute<MiningSpeed>()!!.addEffect(ConstantAttributeEffect(miningSpeed))

        super.setupItem()
    }

    override val ability: ItemAbility? = null

    abstract val breakingPower: Int

    override val headerLore: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            skyblockLore.addLore(breakingPowerLoreComponent)
            skyblockLore.addLore(super.headerLore)

            return skyblockLore
        }

    override fun loreDescription(): SkyblockLore = this.generateAttributeLore(showAttributes)

    private val breakingPowerLoreComponent: Component
        get() =
            Component
                .text("Breaking Power $breakingPower")
                .color(NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false)
}

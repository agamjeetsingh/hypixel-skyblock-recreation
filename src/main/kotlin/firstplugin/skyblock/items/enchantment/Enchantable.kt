package firstplugin.skyblock.items.enchantment

import firstplugin.skyblock.items.enchantment.Enchantable.Companion.MAX_LINE_LENGTH
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

interface Enchantable {
    val normalEnchants: List<Enchantment>

    val ultimateEnchant: Enchantment?

    /**
     * Returns true if and only if the enchantment was successfully applied.
     * An enchantment application could fail if two enchantments conflict with each other.
     **/
    fun applyEnchant(enchantment: Enchantment): Boolean

    fun enchantmentLore(): MutableList<Component>

    companion object {
        const val MAX_LINE_LENGTH = 50
    }
}

class EnchantableDelegate : Enchantable {
    private val _normalEnchants = mutableListOf<Enchantment>()
    private var _ultimateEnchant: Enchantment? = null

    override val normalEnchants: List<Enchantment>
        get() = _normalEnchants.toList()

    override val ultimateEnchant: Enchantment?
        get() = _ultimateEnchant

    override fun applyEnchant(enchantment: Enchantment): Boolean {
        enchantment.conflictsWith.forEach {
            if (it in normalEnchants) {
                return false
            }
            if (it == ultimateEnchant) {
                return false
            }
        }
        // Enchantment doesn't conflict with any other enchantment
        if (enchantment.enchantmentType == EnchantmentType.ULTIMATE) {
            _ultimateEnchant = enchantment
        } else {
            _normalEnchants.add(enchantment)
        }
        return true
    }

    override fun enchantmentLore(): MutableList<Component> {
        val enchants = mutableListOf<Enchantment>()
        val lore: MutableList<Component> = mutableListOf()
        enchants.addAll(_normalEnchants)
        if (_ultimateEnchant != null) {
            enchants.addFirst(_ultimateEnchant!!)
        }
        if (enchants.size > 3) {
            while (enchants.isNotEmpty()) {
                val enchantsInThisLine: List<Enchantment>
                val joinedString = enchants.joinToString(", ")
                val joinedStringTruncated = joinedString.slice(0..<MAX_LINE_LENGTH)
                val enchantsInTruncated: Int = joinedStringTruncated.count { it == ',' } + 1
                enchantsInThisLine = enchants.slice(0..<enchantsInTruncated)
                (0..<enchantsInTruncated).forEach { enchants.removeFirst() }
                val component: Component = Component.text("")
                (0..<enchantsInTruncated).forEach {
                    component.append(enchantsInThisLine[it].formattedForLore())
                    if (it != enchantsInTruncated - 1) {
                        component.append(
                            Component
                                .text(", ")
                                .color(NamedTextColor.BLUE)
                                .decoration(TextDecoration.ITALIC, false),
                        )
                    }
                }
                lore.add(component)
            }
            return lore
        }
        // enchants.size <= 3
        enchants.forEach {
            lore.add(it.formattedForLore())
            lore.addAll(it.description)
        }
        return lore
    }
}

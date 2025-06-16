@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.items

import firstplugin.skyblock.brewing.BrewingIngredient
import firstplugin.skyblock.collection.CollectionItem
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.reforge.Reforgeable
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

abstract class SkyblockItem(
    open val material: Material,
) : ItemStack(material) {
    // Do not initialise in init block - let subclasses initialise first

    /**
     * Call this method from the subclass's init block AFTER all properties have been initialized
     */
    protected open fun setupItem() {
        val meta = itemMeta

        meta.persistentDataContainer.set(
            NamespacedKey("firstplugin", "item.internal_id.${internalID.lowercase()}"),
            PersistentDataType.STRING,
            internalID,
        )

        val name = generateCustomName()
        meta.displayName(name)

        val skyblockLore = generateLore()
        if (skyblockLore.lore.isNotEmpty()) {
            meta.lore(skyblockLore.lore)
        }

        // Hide vanilla attributes
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        // Add dummy attribute to make HIDE_ATTRIBUTES work
        addDummyAttribute(meta)

        // Apply all changes
        itemMeta = meta
    }

    private fun addDummyAttribute(meta: ItemMeta) {
        val modifier =
            AttributeModifier(
                NamespacedKey("dummy", "dummy_modifier"),
                0.0,
                AttributeModifier.Operation.ADD_NUMBER,
            )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier)
    }

    abstract val itemRarity: Rarity

    abstract val internalID: String

    private val rarityComponent: Component
        get() {
            val rarityText: String =
                if (itemCategory != null) {
                    "$itemRarity $itemCategory"
                } else {
                    itemRarity.toString()
                }

            return Component
                .text(rarityText)
                .color(itemRarity.color)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true)
        }

    abstract val holder: ItemHolder

    abstract val itemCategory: ItemCategory?

    open val itemName: String = formattedMaterialName

    private val formattedMaterialName: String
        get() {
            val materialString = material.name.lowercase()
            val words = materialString.split("_")
            val newWords = mutableListOf<String>()
            words.forEach { it1 -> newWords.add(it1.replaceFirstChar { it2 -> it2.uppercaseChar() }) }
            return newWords.joinToString(" ")
        }

    protected open val headerLore: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            if (this is BrewingIngredient) {
                skyblockLore.addLore(brewingIngredientLore)
            }
            if (this is CollectionItem) {
                skyblockLore.addLore(collectionItemLore)
            }
            return skyblockLore
        }

    protected open val footerLore: SkyblockLore
        get() {
            val skyblockLore = SkyblockLore()
            if (this is Reforgeable && !isReforged) {
                skyblockLore.addLore(reforgeableLore)
            }

            skyblockLore.addLore(rarityComponent)
            return skyblockLore
        }

    private fun generateLore(): SkyblockLore {
        val skyblockLore = SkyblockLore()

        skyblockLore.addLore(headerLore)
        if (!headerLore.isEmpty()) {
            skyblockLore.addEmptyLine()
        }

        skyblockLore.addLore(loreDescription())

        if (!loreDescription().isEmpty()) {
            skyblockLore.addEmptyLine()
        }
        skyblockLore.addLore(footerLore)

        return skyblockLore
    }

    open fun loreDescription(): SkyblockLore = SkyblockLore()

    open fun generateCustomName(): TextComponent =
        Component
            .text(itemName)
            .color(itemRarity.color)
            .decoration(TextDecoration.ITALIC, false)
}

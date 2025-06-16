package firstplugin.skyblock.crafting.recipe

import firstplugin.skyblock.crafting.CraftingSlot
import firstplugin.skyblock.crafting.EmptyCraftingSlot
import firstplugin.skyblock.items.SkyblockItem
import kotlin.reflect.KClass

class ShapelessSkyblockRecipe(
    override val craftedItemClass: KClass<out SkyblockItem>,
    craftingSlots: List<CraftingSlot>,
    craftedItemQuantity: Int = 1,
) : SkyblockRecipe() {
    override val craftedItemQuantity: Int = 1.coerceAtLeast(craftedItemQuantity)

    private val _slots: MutableList<CraftingSlot>

    init {
        val mutableSlots = craftingSlots.toMutableList()
        if (mutableSlots.size >= 9) {
            _slots = mutableSlots.subList(0, 8)
        } else {
            (0..<(9 - mutableSlots.size)).forEach { _ ->
                mutableSlots.add(EmptyCraftingSlot())
            }
            _slots = mutableSlots
        }
    }

    override val slots: List<CraftingSlot>
        get() = _slots.toList()

    override fun isSameRecipe(otherSlots: List<CraftingSlot>): Boolean {
        // For shapeless recipes, we only care about the ingredients being the same,
        // not their positions. So we count the occurrences of each item type.
        val thisIngredients = slots.filter { !it.isEmptySlot }
        val otherIngredients = otherSlots.filter { !it.isEmptySlot }

        // First, check if we have the same number of ingredients
        if (thisIngredients.size != otherIngredients.size) {
            return false
        }

        // Group ingredients by item type and count occurrences
        val thisItemCounts = thisIngredients.groupBy { it.item }.mapValues { it.value.size }
        val otherItemCounts = otherIngredients.groupBy { it.item }.mapValues { it.value.size }

        // Check if both recipes have the same items in the same quantities
        return thisItemCounts == otherItemCounts
    }
}

package firstplugin.skyblock.crafting.recipe

import firstplugin.skyblock.crafting.CraftingSlot
import firstplugin.skyblock.crafting.EmptyCraftingSlot
import firstplugin.skyblock.items.SkyblockItem
import kotlin.reflect.KClass

/**
 * Represents a shaped crafting recipe. It's construction inside a [SkyblockItem]
 * would look something like:
 * ```
 * val recipe = ShapedSkyblockRecipe(
 *      this,
 *      TODO()
 * )
 * ```
 *
 */
class ShapedSkyblockRecipe(
    override val craftedItemClass: KClass<out SkyblockItem>,
    craftingSlots: List<CraftingSlot>,
    craftedItemQuantity: Int = 1,
) : SkyblockRecipe() {
    // TODO - Needs rigorous testing

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
        // Convert slots to a 3x3 grid for easier manipulation
        val thisGrid = slotsToGrid(this.slots)
        val otherGrid = slotsToGrid(otherSlots)

        // Check if patterns match with any possible translation
        return canTranslateToMatch(thisGrid, otherGrid)
    }

    private fun slotsToGrid(slots: List<CraftingSlot>): List<List<CraftingSlot>> =
        listOf(
            slots.subList(0, 3),
            slots.subList(3, 6),
            slots.subList(6, 9),
        )

    private fun canTranslateToMatch(
        grid1: List<List<CraftingSlot>>,
        grid2: List<List<CraftingSlot>>,
    ): Boolean {
        // Get the boundaries of non-empty slots in both grids
        val (minRow1, maxRow1, minCol1, maxCol1) = findPatternBoundaries(grid1)
        val (minRow2, maxRow2, minCol2, maxCol2) = findPatternBoundaries(grid2)

        // If pattern dimensions don't match, they can't be the same recipe
        val height1 = maxRow1 - minRow1 + 1
        val width1 = maxCol1 - minCol1 + 1
        val height2 = maxRow2 - minRow2 + 1
        val width2 = maxCol2 - minCol2 + 1

        if (height1 != height2 || width1 != width2) {
            return false
        }

        // Extract the actual patterns (without empty surrounding space)
        val pattern1 = extractPattern(grid1, minRow1, maxRow1, minCol1, maxCol1)
        val pattern2 = extractPattern(grid2, minRow2, maxRow2, minCol2, maxCol2)

        // Check if patterns match exactly
        return patternsEqual(pattern1, pattern2)
    }

    private fun findPatternBoundaries(grid: List<List<CraftingSlot>>): List<Int> {
        var minRow = 3
        var maxRow = -1
        var minCol = 3
        var maxCol = -1

        // Find boundaries of the pattern
        for (row in 0..2) {
            for (col in 0..2) {
                if (!grid[row][col].isEmptySlot) {
                    minRow = minOf(minRow, row)
                    maxRow = maxOf(maxRow, row)
                    minCol = minOf(minCol, col)
                    maxCol = maxOf(maxCol, col)
                }
            }
        }

        // If no items found, return default boundaries
        if (maxRow == -1) {
            return listOf(0, 0, 0, 0)
        }

        return listOf(minRow, maxRow, minCol, maxCol)
    }

    private fun extractPattern(
        grid: List<List<CraftingSlot>>,
        minRow: Int,
        maxRow: Int,
        minCol: Int,
        maxCol: Int,
    ): List<List<CraftingSlot>> {
        val pattern = mutableListOf<List<CraftingSlot>>()
        for (row in minRow..maxRow) {
            val patternRow = mutableListOf<CraftingSlot>()
            for (col in minCol..maxCol) {
                patternRow.add(grid[row][col])
            }
            pattern.add(patternRow)
        }
        return pattern
    }

    private fun patternsEqual(
        pattern1: List<List<CraftingSlot>>,
        pattern2: List<List<CraftingSlot>>,
    ): Boolean {
        if (pattern1.size != pattern2.size || pattern1[0].size != pattern2[0].size) {
            return false
        }

        for (row in pattern1.indices) {
            for (col in pattern1[0].indices) {
                val item1 = pattern1[row][col].item
                val item2 = pattern2[row][col].item

                // Compare items, considering null as a match to null only
                if ((item1 == null && item2 != null) || (item1 != null && item2 == null)) {
                    return false
                }

                if (item1 != null && item2 != null && item1 != item2) {
                    return false
                }
            }
        }
        return true
    }
}

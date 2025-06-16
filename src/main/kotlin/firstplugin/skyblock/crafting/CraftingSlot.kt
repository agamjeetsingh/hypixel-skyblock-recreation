package firstplugin.skyblock.crafting

import firstplugin.skyblock.items.SkyblockItem

open class CraftingSlot(
    val item: SkyblockItem?,
    stackSize: Int,
) {
    val stackSize = 0.coerceAtLeast(stackSize)

    val isEmptySlot = stackSize == 0
}

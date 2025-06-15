package firstplugin.skyblock.crafting

import firstplugin.skyblock.crafting.recipe.SkyblockRecipe
import firstplugin.skyblock.items.SkyblockItem

/**
 * Represents those [SkyblockItem]s that can be crafted from other resources.
 */
interface Craftable {
    val recipe: SkyblockRecipe
}

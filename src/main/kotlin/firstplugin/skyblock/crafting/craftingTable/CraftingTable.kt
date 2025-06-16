package firstplugin.skyblock.crafting.craftingTable

import firstplugin.skyblock.crafting.Crafter
import firstplugin.skyblock.crafting.recipe.SkyblockRecipe
import firstplugin.skyblock.entity.SkyblockPlayer

class CraftingTable(
    val player: SkyblockPlayer,
) {
    val menu = CraftingTableMenu(player)

    fun canCraft(craftingRecipe: SkyblockRecipe): Boolean = canCraft(player, craftingRecipe)

    fun openCraftingMenu() {
        player.openInventory(menu.inventory)
    }

    companion object {
        /**
         * This method also handles the cases where the attempting [SkyblockRecipe] is not real.
         */
        fun canCraft(
            whoIsAttemptingToCraft: Crafter,
            craftingRecipe: SkyblockRecipe,
        ): Boolean = whoIsAttemptingToCraft.unlockedRecipes.find { it.isSameRecipe(craftingRecipe) } != null
    }
}

package firstplugin.skyblock.crafting

import firstplugin.skyblock.crafting.recipe.SkyblockRecipe
import firstplugin.skyblock.entity.SkyblockPlayer

/**
 * Represents entities who can craft, usually [SkyblockPlayer]s.
 */
interface Crafter {
    val unlockedRecipes: List<SkyblockRecipe>

    fun unlockRecipe(newRecipe: SkyblockRecipe)
}

class CrafterDelegate : Crafter {
    private val _unlockedRecipes: MutableList<SkyblockRecipe> = mutableListOf()

    override val unlockedRecipes: List<SkyblockRecipe>
        get() = _unlockedRecipes.toList()

    override fun unlockRecipe(newRecipe: SkyblockRecipe) {
        if (newRecipe !in _unlockedRecipes) {
            _unlockedRecipes.add(newRecipe)
        }
    }
}

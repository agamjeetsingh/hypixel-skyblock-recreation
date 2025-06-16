package firstplugin.skyblock.items.vanilla

import firstplugin.skyblock.crafting.*
import firstplugin.skyblock.crafting.recipe.ShapelessSkyblockRecipe
import firstplugin.skyblock.crafting.recipe.SkyblockRecipe
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.SkyblockItem
import org.bukkit.Material

class OakPlank(
    override val holder: ItemHolder,
) : SkyblockItem(Material.OAK_PLANKS),
    Craftable {
    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "WOOD"
    override val itemCategory: ItemCategory? = null
    override val recipe: SkyblockRecipe =
        ShapelessSkyblockRecipe(
            this::class,
            listOf(
                CraftingSlot(OakLog(holder), 1),
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
            ),
            4,
        )

    init {
        setupItem()
    }
}

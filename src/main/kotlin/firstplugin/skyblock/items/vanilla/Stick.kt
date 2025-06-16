package firstplugin.skyblock.items.vanilla

import firstplugin.skyblock.crafting.*
import firstplugin.skyblock.crafting.recipe.ShapedSkyblockRecipe
import firstplugin.skyblock.crafting.recipe.SkyblockRecipe
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.SkyblockItem
import org.bukkit.Material

class Stick(
    override val holder: ItemHolder,
) : SkyblockItem(Material.STICK),
    Craftable {
    override val itemRarity: Rarity = Rarity.COMMON
    override val internalID: String = "STICK"

    override val itemCategory: ItemCategory? = null

    override val recipe: SkyblockRecipe =
        ShapedSkyblockRecipe(
            this::class,
            listOf(
                CraftingSlot(OakPlank(holder), 1),
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
                CraftingSlot(OakPlank(holder), 1),
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

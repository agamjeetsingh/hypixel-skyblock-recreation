package firstplugin.skyblock.mining.pickaxe

import firstplugin.skyblock.crafting.*
import firstplugin.skyblock.crafting.recipe.ShapedSkyblockRecipe
import firstplugin.skyblock.crafting.recipe.SkyblockRecipe
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.ores.Diamond
import firstplugin.skyblock.items.vanilla.Stick
import org.bukkit.Material

class DiamondPickaxe(
    override val holder: ItemHolder,
) : Pickaxe(Material.DIAMOND_PICKAXE),
    Craftable {
    override val damage: Double = 30.0

    override val miningSpeed: Double = 220.0

    override val breakingPower: Int = 4
    override val itemRarity: Rarity = Rarity.UNCOMMON
    override val internalID: String = "DIAMOND_PICKAXE"

    override val recipe: SkyblockRecipe =
        ShapedSkyblockRecipe(
            this::class,
            listOf(
                CraftingSlot(Diamond(holder), 1),
                CraftingSlot(Diamond(holder), 1),
                CraftingSlot(Diamond(holder), 1),
                EmptyCraftingSlot(),
                CraftingSlot(Stick(holder), 1), // Need sticks, craftedItemQuantity is also a TODO(), Lots of todos...
                EmptyCraftingSlot(),
                EmptyCraftingSlot(),
                CraftingSlot(Stick(holder), 1),
                EmptyCraftingSlot(),
            ),
        )

    init {
        setupItem()
    }
}

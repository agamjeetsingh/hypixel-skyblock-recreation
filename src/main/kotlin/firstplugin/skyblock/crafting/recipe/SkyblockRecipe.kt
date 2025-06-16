package firstplugin.skyblock.crafting.recipe

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.crafting.CraftingSlot
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.items.DummyItemHolder
import firstplugin.skyblock.items.SkyblockItem
import firstplugin.skyblock.rewards.SkyblockReward
import firstplugin.skyblock.utils.StringUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import kotlin.reflect.KClass

typealias RecipeFootPrint = Set<Pair<KClass<out SkyblockItem>, Int>>

abstract class SkyblockRecipe : SkyblockReward {
    abstract val craftedItemClass: KClass<out SkyblockItem>

    abstract val craftedItemQuantity: Int

    /**
     * First 3 elements represent the first row, the next 3 the next row and the last 3 represent the last row
     */
    abstract val slots: List<CraftingSlot>

    fun isSameRecipe(other: SkyblockRecipe): Boolean =
        this.isSameRecipe(other.slots) ||
            (this is ShapelessSkyblockRecipe && other is ShapedSkyblockRecipe) ||
            (this is ShapedSkyblockRecipe && other is ShapelessSkyblockRecipe)

    abstract fun isSameRecipe(otherSlots: List<CraftingSlot>): Boolean

    val footprint: RecipeFootPrint
        get() = createFootPrint(slots)

    override val rewardMessage: Component
        get() =
            Component
                .text(StringUtils.camelCaseToSpaced(craftedItemClass.simpleName!!))
                .color(
                    craftedItemClass.constructors
                        .firstOrNull { constructor ->
                            constructor.parameters.size == 1 &&
                                constructor.parameters[0].type.classifier == ItemHolder::class
                        }?.call(DummyItemHolder())
                        ?.itemRarity
                        ?.color,
                ).decoration(TextDecoration.ITALIC, false)
                .append(
                    Component
                        .text(" Recipe")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                )

    override fun applyRewardTo(
        skyblockPlayer: SkyblockPlayer,
        skyblockPlugin: SkyblockPlugin?,
    ) {
        skyblockPlayer.unlockRecipe(this)
    }

    companion object {
        fun createFootPrint(slots: List<CraftingSlot>): RecipeFootPrint {
            val hashSet = hashSetOf<Pair<KClass<out SkyblockItem>, Int>>()

            for (slot in slots) {
                val skyblockItem = slot.item ?: continue
                val quantity = slot.stackSize
                hashSet.add(skyblockItem::class to quantity)
            }

            return hashSet
        }
    }
}

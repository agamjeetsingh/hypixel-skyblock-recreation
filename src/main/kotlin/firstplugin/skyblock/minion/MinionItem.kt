package firstplugin.skyblock.minion

import firstplugin.skyblock.items.ItemCategory
import firstplugin.skyblock.items.Rarity
import firstplugin.skyblock.items.SkyblockItem
import firstplugin.skyblock.utils.RomanNumerals
import firstplugin.skyblock.utils.SkinUtils
import firstplugin.skyblock.utils.SkyblockLore
import firstplugin.skyblock.utils.StringUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

abstract class MinionItem(
    material: Material,
) : SkyblockItem(material) {
    /**
     * All [MinionItem] have rarity [Rarity.RARE].
     */
    override val itemRarity: Rarity = Rarity.RARE

    override val itemCategory: ItemCategory = ItemCategory.MINION

    abstract val minion: Minion

    /**
     * Represents the tier of the [MinionItem]. It should be initialised to [Minion.tier] and its value
     * should NEVER change.
     */
    abstract val tier: Int

    /**
     * The "Place this minion and it will start ..." text in the item lore.
     * Example: "Place this minion and it will start generating and mining cobblestone!"
     */
    protected open val placeThisMinionComponent: Component
        get() {
            val collectionItemString = minion.collectionItem::class.simpleName?.lowercase()
            var baseComponent =
                when (minion.minionType) {
                    MinionType.MINING ->
                        Component.text(
                            "Place this minion and it will start generating and mining $collectionItemString!",
                        )
                    else -> Component.text("")
                }
            baseComponent = baseComponent.color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            return baseComponent
        }

    /**
     * The texture list for the minion skin
     */
    protected abstract val textures: List<String>

    /**
     * Method to apply the minion skin texture to the player head item.
     * This handles preserving the item's display name and lore after applying the skin.
     */
    private fun applyMinionTexture() {
        // Get the current item meta
        val currentMeta = this.itemMeta

        // Create a player head with the appropriate texture for the current tier
        val headItem =
            textures.getOrNull(tier - 1)?.let { SkinUtils.createPlayerHeadWithTexture(it) }
                ?: return

        val headMeta = headItem.itemMeta

        // Preserve our display name and lore
        val displayName = currentMeta.displayName()
        val lore = currentMeta.lore()

        // Apply the head meta first
        this.setItemMeta(headMeta)

        // Then restore our display name and lore
        val newMeta = this.itemMeta
        newMeta.displayName(displayName)
        newMeta.lore(lore)
        this.setItemMeta(newMeta)
    }

    /**
     * Initialise the minion item by setting up its properties and applying the texture.
     * This should be called in the init block of all subclasses.
     */
    protected fun initialiseMinionItem() {
        // Set up the basic item properties
        setupItem()

        // Apply the texture to the player head while preserving name and lore
        applyMinionTexture()
    }

    /**
     * Part of the item lore.
     */
    private val requiresAnOpenAreaComponent: Component
        get() =
            when {
                minion.minionType == MinionType.MINING ->
                    Component
                        .text("Requires an open area to place ${minion.collectionItem::class.simpleName?.lowercase()}.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                else ->
                    Component
                        .text("Requires an open area to work.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)
            }

    /**
     * Part of the item lore.
     */
    private val minionsWorkWhenOfflineComponent: Component =
        Component
            .text("Minions also work when you are offline!")
            .color(NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false)

    /**
     * Part of the item lore.
     */
    private val timeBetweenActionsComponent: Component by lazy {
        Component
            .text("Time Between Actions: ")
            .color(NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false)
            .append(
                Component
                    .text("${(minion.timeBetweenActions / 20.0).toInt()}s")
                    .color(NamedTextColor.GREEN)
                    .decoration(TextDecoration.ITALIC, false),
            )
    }

    /**
     * Part of the item lore.
     */
    private val maxStorageComponent: Component
        get() =
            Component
                .text("Max Storage: ")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    Component
                        .text(minion.maxStorage)
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false),
                )

    /**
     * Part of the item lore.
     */
    private val resourcesGeneratedComponent: Component
        get() =
            Component
                .text("Resources Generated: ")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    Component
                        .text(minion.resourcesGenerated)
                        .color(NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false),
                )

    /**
     * Generates the lore by combining different lore components using [SkyblockLore].
     */
    override fun loreDescription(): SkyblockLore {
        val skyblockLore = SkyblockLore()
        skyblockLore.addLore(
            placeThisMinionComponent
                .append(requiresAnOpenAreaComponent)
                .append(minionsWorkWhenOfflineComponent),
        )
        skyblockLore.addEmptyLine()
        skyblockLore.addLore(timeBetweenActionsComponent)
        skyblockLore.addLore(maxStorageComponent)
        skyblockLore.addLore(resourcesGeneratedComponent)
        return skyblockLore
    }

    /**
     * Generates the [itemName]. Example: Class name is CobblestoneMinion and tier is 3.
     * The [itemName] would be "Cobblestone Minion III"
     */
    override val itemName: String
        get() {
            var className = StringUtils.camelCaseToSpaced(this.javaClass.simpleName)
            className = className.split(" ").firstOrNull() ?: return "null"
            val tierInRoman = RomanNumerals.toRoman(tier)
            return "$className Minion $tierInRoman"
        }
}

package firstplugin.skyblock.crafting.recipe

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.crafting.Craftable
import firstplugin.skyblock.crafting.CraftingSlot
import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.DummyItemHolder
import firstplugin.skyblock.items.SkyblockItem
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

/**
 * Central registry for all crafting recipes in the game.
 * Allows easy access to recipes by item class.
 */
object CraftingRecipeRegistry {
    private val recipesByClass = hashMapOf<KClass<out SkyblockItem>, SkyblockRecipe>()
    private val recipesByFootprint: HashMap<RecipeFootPrint, List<SkyblockRecipe>> = hashMapOf()
    private val allRecipes = mutableListOf<SkyblockRecipe>()

    /**
     * Register a recipe for a specific SkyblockItem class
     */
    fun registerRecipe(
        itemClass: KClass<out SkyblockItem>,
        recipe: SkyblockRecipe,
    ) {
        recipesByClass[itemClass] = recipe
        recipesByFootprint.merge(recipe.footprint, listOf(recipe)) { existing, new ->
            existing + new
        }
        allRecipes.add(recipe)
    }

    /**
     * Get the recipe for a specific SkyblockItem class
     */
    fun getRecipe(itemClass: KClass<out SkyblockItem>): SkyblockRecipe? = recipesByClass[itemClass]

    fun getRecipe(
        slots: List<CraftingSlot>,
        plugin: SkyblockPlugin,
    ): SkyblockRecipe? {
        plugin.logger.info("Footprints: $recipesByFootprint.toString()")
        val potentialRecipes = recipesByFootprint[SkyblockRecipe.createFootPrint(slots)] ?: return null
        plugin.logger.info("Potential recipes: $potentialRecipes")

        for (potentialRecipe in potentialRecipes) {
            if (potentialRecipe.isSameRecipe(slots)) return potentialRecipe
        }
        return null
    }

    fun getRecipes(footPrint: RecipeFootPrint): List<SkyblockRecipe>? = recipesByFootprint[footPrint]

    /**
     * Get all registered recipes
     */
    fun getAllRecipes(): List<SkyblockRecipe> = allRecipes.toList()

    /**
     * Find a recipe that matches the given crafting slots
     */
    fun findMatchingRecipe(craftingSlots: List<CraftingSlot>): SkyblockRecipe? =
        allRecipes.find { recipe -> recipe.slots == craftingSlots }

    /**
     * Automatically register all recipes from companion objects
     * of SkyblockItem subclasses
     */
    fun autoRegisterRecipes(plugin: SkyblockPlugin) {
        val reflections = Reflections("firstplugin.skyblock")
        val craftableItemClasses =
            reflections
                .getSubTypesOf(SkyblockItem::class.java)
                .filter { Craftable::class.java.isAssignableFrom(it) }
        plugin.logger.info(craftableItemClasses.toString())
        val dummyHolder = DummyItemHolder() // Create holder once

        craftableItemClasses.forEach { clazz ->
            val kClazz = clazz.kotlin // Get Kotlin KClass

            // Skip abstract classes, interfaces, etc.
            if (kClazz.isAbstract || kClazz.java.isInterface || kClazz.objectInstance != null) {
                plugin.logger.info("Skipping non-instantiable class: ${kClazz.simpleName}")
                return@forEach // Continue to next class
            }

            try {
                // Find the primary constructor
                val constructor = kClazz.primaryConstructor
                if (constructor == null) {
                    plugin.logger.warning("Class ${kClazz.simpleName} has no primary constructor. Skipping.")
                    return@forEach
                }

                // --- Check constructor parameters ---
                // This example assumes a single ItemHolder parameter. Adapt if constructors vary.
                if (constructor.parameters.size == 1 &&
                    constructor.parameters[0].type.jvmErasure == ItemHolder::class // Use jvmErasure for comparison
                    // constructor.parameters[0].type.classifier == ItemHolder::class // Alternative check
                ) {
                    // Instantiate the class using the constructor and dummy holder
                    val instance = constructor.call(dummyHolder) as Craftable // Cast to Craftable to access recipe

                    // Access the recipe property directly
                    val recipe = instance.recipe // No reflection needed here!

                    registerRecipe(kClazz, recipe) // Use kClazz (Kotlin KClass)
                    plugin.logger.info("Registered recipe for ${kClazz.simpleName}")
                } else {
                    plugin.logger.warning(
                        "Class ${kClazz.simpleName} constructor doesn't match expected signature (ItemHolder). Skipping.",
                    )
                }
            } catch (e: Exception) {
                plugin.logger.severe("Failed to instantiate or get recipe for ${kClazz.simpleName}: ${e.message}")
                e.printStackTrace() // Print stack trace for detailed debugging
            }
        }
        plugin.logger.info("Recipe Registration Complete. Total recipes: ${allRecipes.size}") // Log final count
        // plugin.logger.info("$allRecipes") // Logging the map might be too verbose
    }
}

/**
 * Extension property to get a recipe for a SkyblockItem class
 */
val <T : SkyblockItem> KClass<T>.recipe: SkyblockRecipe?
    get() = CraftingRecipeRegistry.getRecipe(this)

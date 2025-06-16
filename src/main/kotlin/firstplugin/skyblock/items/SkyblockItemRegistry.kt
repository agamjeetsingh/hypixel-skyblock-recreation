package firstplugin.skyblock.items

import firstplugin.skyblock.entity.ItemHolder
import org.bukkit.inventory.ItemStack
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object SkyblockItemRegistry {
    /**
     * All internal ids are stored in lower case.
     */
    private val itemsByInternalId = HashMap<String, KClass<out SkyblockItem>>()

    val allSkyblockItems: Collection<KClass<out SkyblockItem>>
        get() = itemsByInternalId.values

    fun register(itemClass: KClass<out SkyblockItem>) {
        val internalId = itemClass.primaryConstructor?.call(DummyItemHolder())?.internalID ?: return
        itemsByInternalId[internalId.lowercase()] = itemClass
    }

    fun getItemClass(internalId: String): KClass<out SkyblockItem>? = itemsByInternalId[internalId]

    fun itemStackToSkyblockItem(
        itemStack: ItemStack,
        itemHolder: ItemHolder = DummyItemHolder(),
    ): SkyblockItem? {
        val meta = itemStack.itemMeta ?: return null
        val container = meta.persistentDataContainer

        // Get all keys from the container
        val keys = container.keys

        // Look for a key matching our pattern
        for (key in keys) {
            if (key.namespace == "firstplugin" && key.key.startsWith("item.internal_id.")) {
                // Extract the internal ID from the key
                val internalId = key.key.substringAfter("item.internal_id.")

                // Get the class and create the item
                val itemClass = getItemClass(internalId) ?: continue
                return itemClass.primaryConstructor?.call(itemHolder)
            }
        }

        return null
    }

    /**
     * Automatically register all SkyblockItem classes by instantiating them and getting their internalID
     */
    fun autoRegisterItems() {
        val reflections = Reflections("firstplugin.skyblock")
        val skyblockItemClasses = reflections.getSubTypesOf(SkyblockItem::class.java)

        skyblockItemClasses.forEach { clazz ->
            try {
                // Skip abstract classes
                if (java.lang.reflect.Modifier
                        .isAbstract(clazz.modifiers)
                ) {
                    return@forEach
                }

                register(clazz.kotlin)
            } catch (e: Exception) {
                // Skip classes that can't be registered
            }
        }
    }
}

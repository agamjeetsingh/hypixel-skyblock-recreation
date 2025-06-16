@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes

import firstplugin.skyblock.attributes.dynamicAttributes.Health
import firstplugin.skyblock.attributes.dynamicAttributes.Intelligence
import firstplugin.skyblock.attributes.staticAttributes.*
import firstplugin.skyblock.entity.CombatEntity
import net.kyori.adventure.text.format.NamedTextColor

abstract class Attribute {
    @Transient
    open val attributeHolder: Attributable? = null

    abstract val baseValue: Double

    open val cap = Double.MAX_VALUE

    open val value: Double
        get() {
            var computedValue = Math.max(0.0, baseValue + constantModifiers.sumOf { it.effect })

            val additiveMultiplier = 1.0 + additiveModifiers.sumOf { it.effect }
            computedValue *= (additiveMultiplier)

            val multiplier =
                if (multiplicativeModifiers.isEmpty()) {
                    1.0
                } else {
                    multiplicativeModifiers
                        .map { it.effect }
                        .reduce { acc, value -> acc * value }
                }
            computedValue *= multiplier

            if (computedValue <= 0.0) {
                return 0.0
            }
            return Math.min(cap, computedValue)
        }

    abstract val symbol: String

    abstract val color: NamedTextColor

    val loreColor: NamedTextColor
        get() =
            if (attributeCategory == AttributeCategory.COMBAT ||
                this is Damage
            ) {
                NamedTextColor.RED
            } else {
                NamedTextColor.GREEN
            }

    val constantModifiers: MutableList<ConstantAttributeEffect> =
        mutableListOf()
    val additiveModifiers: MutableList<AdditiveAttributeEffect> =
        mutableListOf()
    val multiplicativeModifiers: MutableList<MultiplicativeAttributeEffect> =
        mutableListOf()

    abstract val attributeCategory: AttributeCategory

    abstract val prettyPrintValueForMenu: String

    open val visibleInSkyblockMenu: Boolean = true

    val isDefault: Boolean
        get() {
            return (constantModifiers.isEmpty()) &&
                (additiveModifiers.isEmpty()) &&
                (multiplicativeModifiers.isEmpty())
        }

    val isNotDefault: Boolean
        get() = !isDefault

    fun addEffect(effect: AttributeEffect) {
        when (effect) {
            is ConstantAttributeEffect -> constantModifiers.add(effect)
            is AdditiveAttributeEffect -> additiveModifiers.add(effect)
            is MultiplicativeAttributeEffect ->
                multiplicativeModifiers.add(
                    effect,
                )
        }
        syncWithBukkit()
    }

    fun removeEffect(effect: AttributeEffect) {
        when (effect) {
            is ConstantAttributeEffect -> constantModifiers.remove(effect)
            is AdditiveAttributeEffect -> additiveModifiers.remove(effect)
            is MultiplicativeAttributeEffect ->
                multiplicativeModifiers.remove(
                    effect,
                )
        }
        syncWithBukkit()
    }

    open fun syncWithBukkit() {
        return
    }

    companion object {
        /**
         * Creates default player attributes using reflection to discover all attribute classes.
         * This approach eliminates the need for AttributeType.
         *
         * @param holder The Attributable that will hold these attributes
         * @return List of all attributes with default values
         */
        fun setupDefaultPlayerAttributes(holder: Attributable): List<Attribute> {
            val attributes =
                discoverAndCreateAttributes(holder, false)
            if (holder is CombatEntity) {
                holder.attributesInitialized = true
            }
            return attributes
        }

        fun setupDefaultItemAttributes(holder: Attributable): List<Attribute> =
            discoverAndCreateAttributes(holder, true)

        /**
         * Uses reflection to discover all attribute classes and instantiate them.
         * This requires all attribute classes to have a constructor that takes Attributable and optionally a Double.
         *
         * @param holder The Attributable that will hold these attributes
         * @param zeroBaseValue If true, uses 0 as the base value (for items), otherwise uses default (for players)
         * @return List of all attributes with appropriate values
         */
        private fun discoverAndCreateAttributes(
            holder: Attributable,
            zeroBaseValue: Boolean = false,
        ): List<Attribute> {
            val attributeClasses =
                allAttributeClasses()
            val result = mutableListOf<Attribute>()

            for (clazz in attributeClasses) {
                try {
                    // For item attributes (baseValue = 0)
                    @Suppress("ktlint:standard:if-else-wrapping")
                    if (zeroBaseValue) {
                        // Try to find constructor that takes Attributable and Double
                        val ctors = clazz.constructors.filter { it.parameters.size == 2 }
                        val ctor = ctors.firstOrNull()

                        if (ctor != null) {
                            val attribute = ctor.call(holder, 0.0)
                            if (attribute is Attribute) {
                                result.add(attribute)
                            }
                        }
                    }
                    // For player attributes (default baseValue)
                    else {
                        // Try any constructor, prioritizing the simplest ones
                        val ctors = clazz.constructors.sortedBy { it.parameters.size }

                        if (ctors.isNotEmpty()) {
                            // For constructor with one parameter (just Attributable)
                            @Suppress("ktlint:standard:if-else-wrapping")
                            if (ctors[0].parameters.size == 1) {
                                val attribute = ctors[0].call(holder)
                                if (attribute is Attribute) {
                                    result.add(attribute)
                                }
                            }
                            // For constructor with two parameters (Attributable, Double)
                            else if (ctors[0].parameters.size == 2) {
                                // Get the constructor's second parameter (baseValue)
                                val param = ctors[0].parameters[1]

                                // Check if the parameter has a default value
                                if (param.isOptional) {
                                    // Just use holder, let the default value be used
                                    try {
                                        val attribute = ctors[0].callBy(mapOf(ctors[0].parameters[0] to holder))
                                        if (attribute is Attribute) {
                                            result.add(attribute)
                                        }
                                    } catch (e: Exception) {
                                        // Fallback to a reasonable default if direct call fails
                                        val attribute = ctors[0].call(holder, 0.0)
                                        if (attribute is Attribute) {
                                            result.add(attribute)
                                        }
                                    }
                                } else {
                                    // No default, use a reasonable default of 0.0
                                    val attribute = ctors[0].call(holder, 0.0)
                                    if (attribute is Attribute) {
                                        result.add(attribute)
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("Failed to create attribute ${clazz.simpleName}: ${e.message}")
                    e.printStackTrace()
                    // Still continue with other attributes
                }
            }

            println("Created ${result.size} attributes out of ${attributeClasses.size} classes")
            return result
        }

        private fun allAttributeClasses(): List<kotlin.reflect.KClass<out Attribute>> =
            listOf(
                Health::class,
                Intelligence::class,
                Defense::class,
                Speed::class,
                Strength::class,
                HealthRegen::class,
                TrueDefense::class,
                CritDamage::class,
                CritChance::class,
                BonusAttackSpeed::class,
                Damage::class,
                FarmingFortune::class,
                MiningFortune::class,
                MiningSpeed::class,
            )

        // Helper to get a user-friendly name from an attribute class
        fun friendlierAttributeName(attribute: Attribute): String {
            val className = attribute::class.simpleName ?: return "Unknown"
            // Convert camel case to spaces
            return className.replace(Regex("([a-z])([A-Z])"), "$1 $2")
        }
    }
}

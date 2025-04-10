package firstplugin.skyblock.attributes

import kotlinx.serialization.Serializable

interface AttributeEffect {
    val effect: Double
}

// TODO Add to whom does it apply to, like sometimes we might have extra damage against zombies specifically
@Serializable
data class BaseAttributeEffect(
    override val effect: Double,
) : AttributeEffect

/**
 * Represents constant attribute modifiers. For example: +20 Defense, +5 Health Points
 */
@Serializable
data class ConstantAttributeEffect(
    private val attributeEffect: AttributeEffect,
) : AttributeEffect by attributeEffect {
    constructor(effect: Double) : this(BaseAttributeEffect(effect))
}

/**
 * Represents additive attribute modifiers. For example:
 * +20% Defense (corresponds to effect = 0.2), +5% Health Points (corresponds to effect = 0.05).
 * In the final calculations, additive attributes are usually added together with 1.0
 * (like 1.0 + 0.2 + 0.05) and multiplied with the main attribute to modify it.
 */
@Serializable
data class AdditiveAttributeEffect(
    private val attributeEffect: AttributeEffect,
) : AttributeEffect by attributeEffect {
    constructor(effect: Double) : this(BaseAttributeEffect(effect))
}

/**
 * Represents multiplicative attribute modifiers. For example:
 * 1.2x Defense (corresponds to effect = 1.2), 1.05x Health Points (corresponds to effect = 1.05).
 * In the final calculations, multiplicative attributes are usually multiplied together (like 1.2 * 1.05)
 * and multiplied with the main attribute to modify it.
 */
@Serializable
data class MultiplicativeAttributeEffect(
    private val attributeEffect: AttributeEffect,
) : AttributeEffect by attributeEffect {
    constructor(effect: Double) : this(BaseAttributeEffect(effect))
}

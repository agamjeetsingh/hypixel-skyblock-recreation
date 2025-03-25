package firstplugin.skyblock.attributes

import kotlinx.serialization.Serializable

interface AttributeEffect {
    val effect: Double
    val id: AttributeType
}

// TODO Add to whom does it apply to, like sometimes we might have extra damage against zombies specifically
@Serializable
class BaseAttributeEffect(
    override val effect: Double,
    override val id: AttributeType,
) : AttributeEffect

/**
 * Represents constant attribute modifiers. For example: +20 Defense, +5 Health Points
 */
@Serializable
class ConstantAttributeEffect(
    private val attributeEffect: AttributeEffect,
) : AttributeEffect by attributeEffect {
    constructor(effect: Double, id: AttributeType) : this(BaseAttributeEffect(effect, id))
}

/**
 * Represents additive attribute modifiers. For example:
 * +20% Defense (corresponds to effect = 0.2), +5% Health Points (corresponds to effect = 0.05).
 * In the final calculations, additive attributes are usually added together with 1.0
 * (like 1.0 + 0.2 + 0.05) and multiplied with the main attribute to modify it.
 */
@Serializable
class AdditiveAttributeEffect(
    private val attributeEffect: AttributeEffect,
) : AttributeEffect by attributeEffect {
    constructor(effect: Double, id: AttributeType) : this(BaseAttributeEffect(effect, id))
}

/**
 * Represents multiplicative attribute modifiers. For example:
 * 1.2x Defense (corresponds to effect = 1.2), 1.05x Health Points (corresponds to effect = 1.05).
 * In the final calculations, multiplicative attributes are usually multiplied together (like 1.2 * 1.05)
 * and multiplied with the main attribute to modify it.
 */
@Serializable
class MultiplicativeAttributeEffect(
    private val attributeEffect: AttributeEffect,
) : AttributeEffect by attributeEffect {
    constructor(effect: Double, id: AttributeType) : this(BaseAttributeEffect(effect, id))
}

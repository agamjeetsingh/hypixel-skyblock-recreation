@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.dynamicAttributes

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.AttributeCategory
import firstplugin.skyblock.attributes.DynamicAttribute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
class Intelligence(
    @Transient
    override val attributeHolder: Attributable? = null,
    override val baseValue: Double = 0.0,
) : DynamicAttribute() {
    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT

    override var current: Double = baseValue
    override val symbol: String = "✎"

    @Transient
    override val color: NamedTextColor = NamedTextColor.AQUA

    val baseMana: Int = 100

    val currentMana: Int
        get() = baseMana + current.toInt()

    val maxMana: Int
        get() = baseMana + max.toInt()

    override val prettyPrintValueForMenu: String = String.format("%.1f", value)
}

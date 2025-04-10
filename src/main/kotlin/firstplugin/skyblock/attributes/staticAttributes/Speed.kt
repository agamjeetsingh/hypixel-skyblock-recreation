@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.AttributeCategory
import firstplugin.skyblock.attributes.StaticAttribute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
class Speed(
    @Transient
    override val attributeHolder: Attributable? = null,
    override val baseValue: Double = 100.0,
) : StaticAttribute() {
    override val attributeCategory: AttributeCategory = AttributeCategory.MISC
    override val symbol: String = "✦"

    @Transient
    override val color: NamedTextColor = NamedTextColor.WHITE

    override val cap: Double = 400.0

    override val prettyPrintValueForMenu: String = value.toInt().toString()

    override fun syncWithBukkit() {
        if (attributeHolder == null) return

        val bukkitSpeedAttribute =
            (attributeHolder as? org.bukkit.attribute.Attributable)?.getAttribute(
                org.bukkit.attribute.Attribute.MOVEMENT_SPEED,
            ) ?: return

        // Use this instance's value directly instead of calling getAttribute again
        bukkitSpeedAttribute.baseValue *= value / 100.0
    }
}

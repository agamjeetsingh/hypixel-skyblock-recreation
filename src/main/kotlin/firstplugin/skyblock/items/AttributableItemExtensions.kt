package firstplugin.skyblock.items

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.Attribute.Companion.friendlierAttributeName
import firstplugin.skyblock.utils.SkyblockLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

fun <T> T.generateAttributeLore(
    showAttributes: List<Class<out Attribute>>,
): SkyblockLore
        where T : Attributable, T : SkyblockItem {
    val skyblockLore = SkyblockLore()
    for (attributeClass in showAttributes) {
        val attribute = attributes.find { attributeClass.isInstance(it) }!!

        if (attribute.isDefault) continue

        val modifier = attribute.constantModifiers.map { it.effect }.sum()

        skyblockLore.addLore(
            Component
                .text("${friendlierAttributeName(attribute)}: ")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    Component
                        .text("+${modifier.toInt()}")
                        .color(attribute.loreColor)
                        .decoration(TextDecoration.ITALIC, false),
                ),
        )
    }

    return skyblockLore
}

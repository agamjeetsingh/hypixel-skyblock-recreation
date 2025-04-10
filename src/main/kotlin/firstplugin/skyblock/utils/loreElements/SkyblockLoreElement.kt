package firstplugin.skyblock.utils.loreElements

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

abstract class SkyblockLoreElement(
    private val componentLore: List<Component>,
) {
    var indentation: Int = 0
        protected set(value) {
            field = 0.coerceAtLeast(value)
        }

    val lore: List<Component>
        get() {
            return componentLore.map { component ->
                if (component is TextComponent) {
                    val indentString = " ".repeat(indentation)
                    Component.text(indentString + component.content()).style(component.style())
                } else {
                    component
                }
            }
        }
}

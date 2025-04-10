package firstplugin.skyblock.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration

class SkyblockComponent private constructor(
    private val component: TextComponent,
) : Component by component {
    companion object {
        fun text(text: String): SkyblockComponent = SkyblockComponent(Component.text(text))

        fun empty(): SkyblockComponent = SkyblockComponent(Component.empty())
    }

    fun color(color: net.kyori.adventure.text.format.TextColor): SkyblockComponent =
        SkyblockComponent(component.color(color))

    override fun decoration(
        decoration: TextDecoration,
        state: Boolean,
    ): SkyblockComponent = SkyblockComponent(component.decoration(decoration, state))

    override fun append(component: Component): SkyblockComponent = SkyblockComponent(this.component.append(component))

    fun append(components: Iterable<Component>): SkyblockComponent {
        val result = this.component.toBuilder()
        components.forEach { result.append(it) }
        return SkyblockComponent(result.build())
    }

    override fun style(style: Style): SkyblockComponent = SkyblockComponent(component.style(style))

    fun hoverEvent(event: net.kyori.adventure.text.event.HoverEvent<*>?): SkyblockComponent =
        SkyblockComponent(component.hoverEvent(event))

    override fun clickEvent(event: net.kyori.adventure.text.event.ClickEvent?): SkyblockComponent =
        SkyblockComponent(component.clickEvent(event))

    fun content(): String = component.content()

    override fun toString(): String = component.toString()
}

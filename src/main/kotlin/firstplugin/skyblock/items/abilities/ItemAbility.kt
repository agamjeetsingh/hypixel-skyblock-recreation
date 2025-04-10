package firstplugin.skyblock.items.abilities

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

interface ItemAbility {
    /**
     * Some abilities don't have any titles but only a description.
     * They are buffs or debuffs that always work and don't need to be activated.
     */
    val title: Component?

    val description: MutableList<Component>

    val abilityLore: MutableList<Component>
        get() {
            if (title == null) {
                return description
            }
            val lore: MutableList<Component> = mutableListOf()
            var abilityTitleComponent =
                Component
                    .text("Ability: ")
                    .color(NamedTextColor.GOLD)
                    .decoration(TextDecoration.ITALIC, false)
                    .append(title!!)
            if (this is RightClickItemAbility) {
                val rightClickComponent =
                    Component
                        .text("  RIGHT CLICK")
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true)
                abilityTitleComponent = abilityTitleComponent.append(rightClickComponent)
            }
            lore.add(abilityTitleComponent)
            lore.addAll(description)
            return lore
        }
}

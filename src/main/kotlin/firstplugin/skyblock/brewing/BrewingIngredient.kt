package firstplugin.skyblock.brewing

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

interface BrewingIngredient {
    val brewingIngredientLore: Component
        get() =
            Component
                .text("Brewing Ingredient")
                .color(NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false)
}

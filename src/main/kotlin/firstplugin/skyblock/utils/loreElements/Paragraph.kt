package firstplugin.skyblock.utils.loreElements

import net.kyori.adventure.text.Component

class Paragraph(
    componentLore: List<Component>,
    newIndentation: Int = 0,
) : SkyblockLoreElement(componentLore) {
    init {
        indentation = newIndentation
    }
}

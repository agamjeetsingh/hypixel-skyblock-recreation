package firstplugin.skyblock.utils.loreElements

import net.kyori.adventure.text.Component

class BulletPoint(
    componentLore: Component,
    newIndentation: Int = 0,
) : SkyblockLoreElement(listOf(componentLore)) {
    init {
        indentation = newIndentation
    }
}

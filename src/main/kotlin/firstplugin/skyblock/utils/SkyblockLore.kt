package firstplugin.skyblock.utils

import firstplugin.skyblock.utils.loreElements.SkyblockLoreElement
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player

// TODO - Might need to add a space between sentences of different components (and in some other cases too, think hard!)

class SkyblockLore(
    private val maxLineLength: Int = INVENTORY_MAX_LINE_LENGTH,
) {
    private val _lore: MutableList<Component> = mutableListOf()
    private val indentations: List<Int>
        get() {
            val previousIndent = 0
            val indentationList = mutableListOf<Int>()
            for (index in 0..<_lore.size) {
                val component = _lore[index]
                if (component is TextComponent) {
                    if (component.content().isEmpty()) {
                        indentationList.add(previousIndent)
                        continue
                    }
                    indentationList.add(component.content().takeWhile { it.isWhitespace() }.count())
                    continue
                } else {
                    indentationList.add(0)
                }
            }
            return indentationList
        }

    val lore: List<Component>
        get() = _lore.toList()

    val lastIndent: Int
        get() = indentations.lastOrNull() ?: 0

    fun isEmpty(): Boolean = _lore.isEmpty()

    fun addEmptyLine() {
        _lore.add(Component.text(""))
    }

    fun addLore(newLore: SkyblockLore) {
        _lore.addAll(newLore.lore)
    }

    private fun addLore(
        newLore: List<Component>,
        newLoreIndentation: Int = 0,
    ) {
        for (component in newLore) {
            val indentedComponent = addIndentation(component, newLoreIndentation)
            _lore.addAll(wrapComponent(indentedComponent, maxLineLength))
        }
    }

    private fun addLore(
        newLore: Component,
        newLoreIndentation: Int = 0,
    ) {
        addLore(listOf(newLore), newLoreIndentation)
    }

    // TODO - Probably should remove SkyblockLoreElement
    fun addLore(loreElement: SkyblockLoreElement) {
        addLore(loreElement.lore, loreElement.indentation)
    }

    /**
     * Master addLore
     */
    fun addLore(
        newLore: List<Component>,
        newIndent: Int = 0,
        maintainIndent: Boolean = false,
    ) {
        val safeNewIndent = 0.coerceAtLeast(newIndent)
        if (safeNewIndent > 0) {
            // We maintain indent always
            addLore(newLore, lastIndent + safeNewIndent)
        } else if (maintainIndent) {
            // No new indent, but still need to maintain indent
            addLore(newLore, lastIndent)
        } else {
            // Just add the new lore
            addLore(newLore)
        }
    }

    fun addLore(
        newLore: Component,
        newIndent: Int = 0,
        maintainIndent: Boolean = false,
    ) {
        addLore(listOf(newLore), newIndent, maintainIndent)
    }

    /**
     * Wraps a component's text to fit within a specified maximum line length.
     * Preserves styling and handles words that exceed the line length.
     * Processes children inline with their own styling.
     *
     * @param component The component to wrap
     * @param maxCharactersPerLine Maximum characters allowed per line
     * @return List of wrapped components
     */
    private fun wrapComponent(
        component: Component,
        maxCharactersPerLine: Int,
    ): List<Component> {
        val result = mutableListOf<Component>()
        var currentLine = Component.empty()
        var currentLineLength = 0
        var isFirstComponentInLine = true

        fun processComponent(comp: Component) {
            if (comp is TextComponent) {
                val content = comp.content()
                val style = comp.style()

                // Handle indentation for the first component
                val leadingSpaces = content.takeWhile { it.isWhitespace() }
                val trimmedContent = content.dropWhile { it.isWhitespace() }

                // If this is the first content on the line, add the indentation
                if (currentLineLength == 0 && leadingSpaces.isNotEmpty()) {
                    currentLine = currentLine.append(Component.text(leadingSpaces).style(style))
                    currentLineLength = leadingSpaces.length
                }

                val words = if (trimmedContent.isNotEmpty()) trimmedContent.split(" ") else listOf("")

                for (word in words) {
                    // Only add a space if this is not the first component or word in the line
                    // and the current word is not empty (to avoid extra spaces)
                    val spaceNeeded =
                        if (currentLineLength > 0 &&
                            !isFirstComponentInLine &&
                            word.isNotEmpty()
                        ) {
                            1
                        } else {
                            0
                        }

                    // Don't mark as first component anymore after processing the first word
                    if (isFirstComponentInLine && word.isNotEmpty()) {
                        isFirstComponentInLine = false
                    }

                    // Check if adding this word would exceed the line limit
                    if (currentLineLength + spaceNeeded + word.length > maxCharactersPerLine) {
                        // Add current line to results if not empty
                        if (currentLineLength > 0) {
                            result.add(currentLine)
                            currentLine = Component.empty()
                            currentLineLength = 0
                            isFirstComponentInLine = true

                            // Add indentation to the new line
                            if (leadingSpaces.isNotEmpty()) {
                                currentLine = currentLine.append(Component.text(leadingSpaces).style(style))
                                currentLineLength = leadingSpaces.length
                            }
                        }

                        // If the word itself is longer than the max line length (minus indentation)
                        if (word.length > maxCharactersPerLine - leadingSpaces.length) {
                            var remainingWord = word
                            while (remainingWord.isNotEmpty()) {
                                val maxChunkSize = maxCharactersPerLine - leadingSpaces.length
                                val chunk = remainingWord.take(maxChunkSize)

                                // If current line already has content
                                if (currentLineLength > 0) {
                                    result.add(currentLine)
                                    currentLine = Component.empty()
                                    currentLineLength = 0
                                    isFirstComponentInLine = true
                                    currentLine = currentLine.append(Component.text(leadingSpaces + chunk).style(style))
                                } else {
                                    currentLine = currentLine.append(Component.text(leadingSpaces + chunk).style(style))
                                }

                                currentLineLength = leadingSpaces.length + chunk.length
                                remainingWord = remainingWord.drop(maxChunkSize)

                                // If there's more of the word, add this line and prepare for the next chunk
                                if (remainingWord.isNotEmpty()) {
                                    result.add(currentLine)
                                    currentLine = Component.empty()
                                    currentLineLength = 0
                                    isFirstComponentInLine = true
                                }
                            }
                        } else {
                            // Word fits on a new line
                            currentLine = currentLine.append(Component.text(word).style(style))
                            currentLineLength += word.length
                            isFirstComponentInLine = false
                        }
                    } else {
                        // Add a space if the line already has content and this is not the first component
                        if (spaceNeeded > 0) {
                            currentLine = currentLine.append(Component.space())
                            currentLineLength++
                        }

                        // Add the word
                        if (word.isNotEmpty()) {
                            currentLine = currentLine.append(Component.text(word).style(style))
                            currentLineLength += word.length
                            isFirstComponentInLine = false
                        }
                    }
                }
            }

            // Process all children of this component inline
            for (child in comp.children()) {
                processComponent(child)
            }
        }

        // Start processing the root component
        processComponent(component)

        // Add the last line if it has content
        if (currentLineLength > 0) {
            result.add(currentLine)
        }

        return result
    }

    /**
     * Adds a specified number of spaces to the beginning of a component's text.
     * Preserves the original component's styling.
     *
     * @param component The component to indent
     * @param indentCount Number of spaces to add as indentation
     * @return New indented component with preserved styling
     */
    private fun addIndentation(
        component: Component,
        indentCount: Int,
    ): Component {
        if (component !is TextComponent || indentCount <= 0) {
            return component
        }

        val indentString = " ".repeat(indentCount)
        return Component
            .text(indentString + component.content())
            .style(component.style())
            .children(component.children()) // Preserve children
    }

    companion object {
        const val MENU_MAX_LINE_LENGTH = 40

        const val INVENTORY_MAX_LINE_LENGTH = 50
    }
}

fun Player.sendMessage(skyblockLore: SkyblockLore) {
    skyblockLore.lore.forEach { this.sendMessage(it) }
}

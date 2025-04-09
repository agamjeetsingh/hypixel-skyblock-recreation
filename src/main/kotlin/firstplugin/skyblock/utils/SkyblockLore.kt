package firstplugin.skyblock.utils

import firstplugin.skyblock.utils.loreElements.SkyblockLoreElement
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

// TODO - Might need to add a space between sentences of different components (and in some other cases too, think hard!)

class SkyblockLore(
    private val maxLineLength: Int = INVENTORY_MAX_LINE_LENGTH,
) {
    private val _lore: MutableList<Component> = mutableListOf()
    private val indentations: List<Int>
        get() =
            _lore.map { component ->
                if (component is TextComponent) {
                    val content = component.content()
                    content.takeWhile { it.isWhitespace() }.count()
                } else {
                    0
                }
            }

    val lore: List<Component>
        get() = _lore.toList()

    val lastIndent: Int
        get() = indentations.lastOrNull() ?: 0

    fun addLore(
        newLore: List<Component>,
        newLoreIndentation: Int? = null,
    ) {
        for (component in newLore) {
            val indentedComponent = addIndentation(component, newLoreIndentation ?: 0)
            _lore.addAll(wrapComponent(indentedComponent, maxLineLength))
        }
    }

    fun addLore(
        newLore: Component,
        newLoreIndentation: Int? = null,
    ) {
        addLore(listOf(newLore), newLoreIndentation)
    }

    // TODO - Probably should remove SkyblockLoreElement
    fun addLore(loreElement: SkyblockLoreElement) {
        addLore(loreElement.lore, loreElement.indentation)
    }

    /**
     * Adds lore components with the same indentation as the last existing
     * lore line, wrapping text to fit within maxLineLength.
     */
    fun addLoreMaintainIndent(newLore: List<Component>) {
        addLore(newLore, lastIndent)
    }

    fun addLoreOneMoreIndent(newLore: List<Component>) {
        addLore(newLore, lastIndent + 1)
    }

    fun addLoreOneLessIndent(newLore: List<Component>) {
        addLore(newLore, lastIndent - 1)
    }

    /**
     * Wraps a component's text to fit within a specified maximum line length.
     * Preserves styling and handles words that exceed the line length.
     *
     * @param component The component to wrap
     * @param maxCharactersPerLine Maximum characters allowed per line
     * @return List of wrapped components
     */
    private fun wrapComponent(
        component: Component,
        maxCharactersPerLine: Int,
    ): List<Component> {
        if (component !is TextComponent) {
            return listOf(component)
        }

        val result = mutableListOf<Component>()
        val content = component.content()
        val style = component.style()

        val leadingSpaces = content.takeWhile { it.isWhitespace() }

        val trimmedContent = content.dropWhile { it.isWhitespace() }
        val words = if (trimmedContent.isNotEmpty()) trimmedContent.split(" ") else listOf("")

        // Start with leading spaces
        var currentLine = StringBuilder(leadingSpaces)

        for (word in words) {
            // Check if adding this word would exceed the line limit
            if (currentLine.length + word.length + (if (currentLine.length > leadingSpaces.length) 1 else 0) >
                maxCharactersPerLine
            ) {
                // Add the current line to results if not empty
                if (currentLine.isNotEmpty()) {
                    result.add(Component.text(currentLine.toString()).style(style))
                    // Start a new line with the same indentation
                    currentLine = StringBuilder(leadingSpaces)
                }

                // If the word itself is longer than the max line length (minus indentation), split it
                if (word.length > maxCharactersPerLine - leadingSpaces.length) {
                    var remainingWord = word
                    while (remainingWord.isNotEmpty()) {
                        val maxChunkSize = maxCharactersPerLine - leadingSpaces.length
                        val chunk = remainingWord.take(maxChunkSize)

                        // Always add indentation to new chunks
                        result.add(Component.text(leadingSpaces + chunk).style(style))
                        remainingWord = remainingWord.drop(maxChunkSize)
                    }
                } else {
                    currentLine.append(word)
                }
            } else {
                // Add a space if the line already has content beyond indentation
                if (currentLine.length > leadingSpaces.length) {
                    currentLine.append(" ")
                }
                currentLine.append(word)
            }
        }

        // Add the last line if not empty
        if (currentLine.isNotEmpty()) {
            result.add(Component.text(currentLine.toString()).style(style))
        }

        // Process children components if any
        val children = component.children()
        if (children.isNotEmpty()) {
            for (child in children) {
                result.addAll(wrapComponent(child, maxCharactersPerLine))
            }
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
        return Component.text(indentString + component.content()).style(component.style())
    }

    companion object {
        const val MENU_MAX_LINE_LENGTH = 40

        const val INVENTORY_MAX_LINE_LENGTH = 50
    }
}

package firstplugin.skyblock.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

object ComponentUtils {
    /**
     * Wraps a Component into multiple lines, preserving formatting.
     * Words that would cross the maxCharactersPerLine limit are pushed to the next line.
     *
     * @param component The component to wrap
     * @param maxCharactersPerLine Maximum characters per line
     * @return List of Components with proper word breaks
     */
    fun wrapComponent(
        component: Component,
        maxCharactersPerLine: Int,
    ): List<Component> {
        if (component !is TextComponent) {
            // Handle non-text components by returning them as is
            return listOf(component)
        }

        val result = mutableListOf<Component>()
        val content = component.content()
        val style = component.style()

        // Split content into words
        val words = content.split(" ")

        var currentLine = StringBuilder()
        var currentLineComponent = Component.empty().style(style)

        for (word in words) {
            // Check if adding this word would exceed the line limit
            if (currentLine.length + word.length + (if (currentLine.isNotEmpty()) 1 else 0) > maxCharactersPerLine) {
                // Add the current line to results if not empty
                if (currentLine.isNotEmpty()) {
                    result.add(Component.text(currentLine.toString()).style(style))
                    currentLine = StringBuilder()
                    currentLineComponent = Component.empty().style(style)
                }

                // If the word itself is longer than the max line length, split it
                if (word.length > maxCharactersPerLine) {
                    var remainingWord = word
                    while (remainingWord.isNotEmpty()) {
                        val chunk = remainingWord.take(maxCharactersPerLine)
                        result.add(Component.text(chunk).style(style))
                        remainingWord = remainingWord.drop(maxCharactersPerLine)
                    }
                } else {
                    currentLine.append(word)
                }
            } else {
                // Add a space if the line is not empty
                if (currentLine.isNotEmpty()) {
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
     * Combines multiple components into a single component with line breaks
     *
     * @param components The list of components to combine
     * @return A single component with line breaks between each original component
     */
    fun combineComponentsWithLineBreaks(components: List<Component>): Component {
        if (components.isEmpty()) return Component.empty()

        val result = Component.empty()
        val builder = result.toBuilder()

        for (i in components.indices) {
            builder.append(components[i])
            if (i < components.size - 1) {
                builder.append(Component.newline())
            }
        }

        return builder.build()
    }

    /**
     * Wraps a component into multiple lines and returns a single component with line breaks
     *
     * @param component The component to wrap
     * @param maxCharactersPerLine Maximum characters per line
     * @return A single component with proper line breaks
     */
    fun wrapComponentWithLineBreaks(
        component: Component,
        maxCharactersPerLine: Int,
    ): Component {
        val wrappedLines = wrapComponent(component, maxCharactersPerLine)
        return combineComponentsWithLineBreaks(wrappedLines)
    }
}

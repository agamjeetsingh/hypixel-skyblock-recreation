package firstplugin

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor

enum class Rank(
    private val prefix: String,
    val colorOfRank: NamedTextColor = NamedTextColor.GRAY,
    val chatColor: NamedTextColor = NamedTextColor.WHITE,
) {
    DEFAULT("", chatColor = NamedTextColor.GRAY),
    VIP("VIP", NamedTextColor.GREEN),
    VIP_PLUS("VIP", NamedTextColor.GREEN),
    MVP("MVP", NamedTextColor.AQUA),
    MVP_PLUS("MVP", NamedTextColor.AQUA),
    MVP_PLUS_PLUS("MVP", NamedTextColor.GOLD),
    OWNER("OWNER", NamedTextColor.RED),
    ;

    fun formatted(): TextComponent =
        when (this) {
            DEFAULT -> Component.text("")
            VIP_PLUS, MVP_PLUS ->
                Component
                    .text("[")
                    .color(colorOfRank)
                    .append(Component.text(prefix).color(colorOfRank))
                    .append(Component.text("+").color(NamedTextColor.GOLD))
                    .append(Component.text("] ").color(colorOfRank))
            VIP, MVP ->
                Component
                    .text("[")
                    .color(colorOfRank)
                    .append(Component.text(prefix).color(colorOfRank))
                    .append(Component.text("] ").color(colorOfRank))
            MVP_PLUS_PLUS ->
                Component
                    .text("[")
                    .color(colorOfRank)
                    .append(Component.text(prefix).color(colorOfRank))
                    .append(Component.text("++").color(NamedTextColor.BLACK))
                    .append(Component.text("] ").color(colorOfRank))
            OWNER ->
                Component
                    .text("[")
                    .color(colorOfRank)
                    .append(Component.text(prefix).color(colorOfRank))
                    .append(Component.text("] ").color(colorOfRank))
        }
}

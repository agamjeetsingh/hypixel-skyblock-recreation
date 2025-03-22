package firstplugin.legacy.minigames.disasters

import firstplugin.legacy.minigames.Game
import org.bukkit.World
import org.bukkit.plugin.Plugin

interface Disaster {
    val game: Game

    val plugin: Plugin
        get() = game.plugin

    val world: World
        get() = game.world

    fun start()

    fun stop()
}

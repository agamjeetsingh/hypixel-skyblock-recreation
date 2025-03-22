package firstplugin.legacy.hotPotato

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class HotPotatoCommand(
    private val manager: HotPotatoManager,
) : BukkitCommand("joingame") {
    init {
        description = "Join the Hot Potato minigame"
        usageMessage = "/joingame"
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String,
        args: Array<out String>,
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("This command can only be used by players").color(NamedTextColor.RED))
            return true
        }

        val player = sender
        manager.addPlayer(player)

        return true
    }
}

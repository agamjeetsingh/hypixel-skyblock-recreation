package firstplugin.skyblock

import firstplugin.ServerPlayer
import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.Attribute.Companion.setupDefaultAttributes
import org.bukkit.entity.Player

class SkyblockPlayer(
    val serverPlayer: ServerPlayer,
) : Player by serverPlayer {
    constructor(player: Player) : this(ServerPlayer(player))

    /**
     * Map of all player attributes by ID
     */
    private val _attributes: MutableList<Attribute> = mutableListOf()

    val attributes: List<Attribute>
        get() = _attributes.toList()

    init {
        // Initialize default attributes
        setupDefaultAttributes()
    }

    /**
     * Gets the player's bukkit player instance
     */
    val bukkitPlayer: Player
        get() = serverPlayer.bukkitPlayer

    /**
     * Gets an attribute by its ID.
     */
    fun getAttribute(id: String): Attribute? = _attributes.find { it.attributeID == id }

    /**
     * Adds a new attribute to the player.
     */
    fun addAttribute(attribute: Attribute) {
        _attributes.add(attribute)
    }
}

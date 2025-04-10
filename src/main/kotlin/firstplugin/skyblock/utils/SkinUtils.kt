package firstplugin.skyblock.utils

import com.destroystokyo.paper.profile.ProfileProperty
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.util.Base64
import java.util.UUID

/**
 * Utility functions for working with Minecraft skin data
 */
object SkinUtils {
    /**
     * Decodes a Base64-encoded Minecraft skin value to extract the skin URL
     *
     * @param value The Base64-encoded skin value string
     * @return The URL of the skin texture, or null if parsing fails
     */
    fun getSkinUrl(value: String): String? =
        try {
            val decoded = String(Base64.getDecoder().decode(value))
            val parser = JSONParser()
            val json = parser.parse(decoded) as JSONObject

            val textures = json["textures"] as JSONObject
            val skin = textures["SKIN"] as JSONObject
            skin["url"] as String
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    /**
     * Decodes a Base64-encoded Minecraft skin value to JSON string
     *
     * @param value The Base64-encoded skin value string
     * @return The decoded JSON string, or null if decoding fails
     */
    fun decodeSkinValue(value: String): String? =
        try {
            String(Base64.getDecoder().decode(value))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    /**
     * Creates a player head ItemStack with a custom texture
     *
     * @param textureValue The Base64-encoded texture value to apply to the head
     * @return An ItemStack of a player head with the texture applied
     */
    fun createPlayerHeadWithTexture(textureValue: String): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD)
        val meta = item.itemMeta as SkullMeta

        // Apply texture to skull
        val profile = Bukkit.createProfile(UUID.randomUUID(), null)
        profile.setProperty(ProfileProperty("textures", textureValue))
        meta.playerProfile = profile
        item.itemMeta = meta

        return item
    }
}

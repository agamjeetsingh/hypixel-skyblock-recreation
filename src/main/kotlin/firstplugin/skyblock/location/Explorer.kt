package firstplugin.skyblock.location

import firstplugin.skyblock.entity.SkyblockPlayer

interface Explorer {
    val locationsDiscovered: List<SkyblockLocation>

    val zonesDiscovered: List<Zone>

    fun discoverLocation(location: SkyblockLocation)

    fun discoverZone(zone: Zone)
}

class ExplorerDelegate(
    private val player: SkyblockPlayer,
) : Explorer {
    private val _locationsDiscovered: MutableList<SkyblockLocation> = mutableListOf()
    override val locationsDiscovered: List<SkyblockLocation>
        get() = _locationsDiscovered.toList()

    private val _zonesDiscovered: MutableList<Zone> = mutableListOf()
    override val zonesDiscovered: List<Zone>
        get() = _zonesDiscovered.toList()

    override fun discoverLocation(location: SkyblockLocation) {
        if (location !in _locationsDiscovered) {
            _locationsDiscovered.add(location)
            location.discoveryText.lore.forEach { player.sendMessage(it) }
            // TODO - Add reward sound effects too
        }
    }

    override fun discoverZone(zone: Zone) {
        if (zone !in _zonesDiscovered) {
            _zonesDiscovered.add(zone)
            zone.discoveryText.lore.forEach { player.sendMessage(it) }
            // TODO - Add reward sound effects too
        }
    }
}

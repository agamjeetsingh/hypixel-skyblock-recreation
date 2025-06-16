package firstplugin.skyblock.location

interface Explorer {
    val locationsDiscovered: List<SkyblockLocation>

    val zonesDiscovered: List<Zone>

    fun discoverLocation(location: SkyblockLocation)

    fun discoverZone(zone: Zone)
}

class ExplorerDelegate : Explorer {
    private val _locationsDiscovered: MutableList<SkyblockLocation> = mutableListOf()
    override val locationsDiscovered: List<SkyblockLocation>
        get() = _locationsDiscovered.toList()

    private val _zonesDiscovered: MutableList<Zone> = mutableListOf()
    override val zonesDiscovered: List<Zone>
        get() = _zonesDiscovered.toList()

    override fun discoverLocation(location: SkyblockLocation) {
        if (location !in _locationsDiscovered) {
            _locationsDiscovered.add(location)
            // TODO - Add reward messages too
        }
    }

    override fun discoverZone(zone: Zone) {
        if (zone !in _zonesDiscovered) {
            _zonesDiscovered.add(zone)
            // TODO - Add reward messages too
        }
    }
}

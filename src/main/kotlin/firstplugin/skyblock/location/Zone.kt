package firstplugin.skyblock.location

/**
 * Zones are areas inside a location. The difference between a Zone and a Location is that
 * unlike Zones, Locations have skill requirements. So if a player fulfills the skill requirement of
 * a Location, then they can access any Zone inside that Location
 */
abstract class Zone : SkyblockArea() {
    abstract val location: SkyblockLocation
}

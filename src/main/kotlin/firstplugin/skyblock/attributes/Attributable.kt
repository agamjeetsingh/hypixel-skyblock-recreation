package firstplugin.skyblock.attributes

interface Attributable {
    val attributes: List<Attribute>
}

inline fun <reified T : Attribute> Collection<Attribute>.getAttribute(): T? =
    firstOrNull {
        it is T
    } as T?

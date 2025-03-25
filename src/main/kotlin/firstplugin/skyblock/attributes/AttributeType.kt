@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes

import firstplugin.skyblock.attributes.dynamicAttributes.Health
import firstplugin.skyblock.attributes.dynamicAttributes.Intelligence
import firstplugin.skyblock.attributes.staticAttributes.*

enum class AttributeType(
    val createAttribute: () -> Attribute,
) {
    HEALTH({ Health() }),
    DEFENSE({ Defense() }),
    SPEED({ Speed() }),
    STRENGTH({ Strength() }),
    INTELLIGENCE({ Intelligence() }),
    HEALTH_REGEN({ HealthRegen() }),
    TRUE_DEFENSE({ TrueDefense() }),
    CRIT_DAMAGE({ CritDamage() }),
    DAMAGE({ Damage() }),
}

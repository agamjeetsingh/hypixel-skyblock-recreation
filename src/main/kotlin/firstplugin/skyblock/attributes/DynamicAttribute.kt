package firstplugin.skyblock.attributes

import kotlinx.serialization.Serializable

@Serializable
abstract class DynamicAttribute : Attribute() {
    abstract var current: Double

    val max: Double
        get() = value

    open fun increase(increaseAmt: Double) {
        current = Math.min(max, current + increaseAmt)
    }

    open fun decrease(decreaseAmt: Double) {
        if (decreaseAmt <= 0.0) {
            return
        }
        current = Math.max(0.0, current - decreaseAmt)
    }
}

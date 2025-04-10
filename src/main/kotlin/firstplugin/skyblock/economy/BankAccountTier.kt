package firstplugin.skyblock.economy

import net.kyori.adventure.text.format.NamedTextColor

// TODO Add unlock requirements

enum class BankAccountTier(
    val maxBalance: Long,
    val interestFunction: (Long) -> Long,
    val maxInterest: Long,
    val maxInterestWithBalance: Long,
    val color: NamedTextColor,
) {
    STARTER(
        50_000_000L,
        { balance ->
            // First 10M yields 2% interest, 10M-15M yields 1% interest
            val firstTierInterest = minOf(balance, 10_000_000L) * 2L / 100L
            val secondTierAmount = maxOf(0L, minOf(balance - 10_000_000L, 5_000_000L))
            val secondTierInterest = secondTierAmount * 1L / 100L

            firstTierInterest + secondTierInterest
        },
        250_000L,
        15_000_000L,
        NamedTextColor.GREEN,
    ),
    GOLD(
        100_000_000L,
        { balance ->
            // First 10M yields 2% interest, 10M-20M yields 1% interest
            val firstTierInterest = minOf(balance, 10_000_000L) * 2L / 100L
            val secondTierAmount = maxOf(0L, minOf(balance - 10_000_000L, 10_000_000L))
            val secondTierInterest = secondTierAmount * 1L / 100L

            firstTierInterest + secondTierInterest
        },
        300_000L,
        20_000_000L,
        NamedTextColor.GOLD,
    ),
    DELUXE(
        250_000_000L,
        { balance ->
            // First 10M yields 2% interest, 10M-20M yields 1% interest
            // 20M-30M yields 0.5% interest
            val firstTierInterest = minOf(balance, 10_000_000L) * 2L / 100L
            val secondTierAmount = maxOf(0L, minOf(balance - 10_000_000L, 10_000_000L))
            val secondTierInterest = secondTierAmount * 1L / 100L
            val thirdTierAmount = maxOf(0L, minOf(balance - 20_000_000L, 10_000_000L))
            val thirdTierInterest = thirdTierAmount * 5L / 1000L

            firstTierInterest + secondTierInterest + thirdTierInterest
        },
        350_000L,
        30_000_000L,
        NamedTextColor.LIGHT_PURPLE,
    ),
    SUPER_DELUXE(
        500_000_000L,
        { balance ->
            // First 10M yields 2% interest, 10M-20M yields 1% interest
            // 20M-30M yields 0.5% interest, 30M-50M yields 0.2% interest
            val firstTierInterest = minOf(balance, 10_000_000L) * 2L / 100L
            val secondTierAmount = maxOf(0L, minOf(balance - 10_000_000L, 10_000_000L))
            val secondTierInterest = secondTierAmount * 1L / 100L
            val thirdTierAmount = maxOf(0L, minOf(balance - 20_000_000L, 10_000_000L))
            val thirdTierInterest = thirdTierAmount * 5L / 1000L
            val fourthTierAmount = maxOf(0L, minOf(balance - 30_000_000L, 20_000_000L))
            val fourthTierInterest = fourthTierAmount * 2L / 1000L

            firstTierInterest + secondTierInterest + thirdTierInterest + fourthTierInterest
        },
        390_000L,
        50_000_000L,
        NamedTextColor.DARK_PURPLE,
    ),
    PREMIER(
        1_000_000_000L,
        { balance ->
            // First 10M yields 2% interest, 10M-20M yields 1% interest
            // 20M-30M yields 0.5% interest, 30M-50M yields 0.2% interest
            // 50M-160M yields 0.1% interest
            val firstTierInterest = minOf(balance, 10_000_000L) * 2L / 100L
            val secondTierAmount = maxOf(0L, minOf(balance - 10_000_000L, 10_000_000L))
            val secondTierInterest = secondTierAmount * 1L / 100L
            val thirdTierAmount = maxOf(0L, minOf(balance - 20_000_000L, 10_000_000L))
            val thirdTierInterest = thirdTierAmount * 5L / 1000L
            val fourthTierAmount = maxOf(0L, minOf(balance - 30_000_000L, 20_000_000L))
            val fourthTierInterest = fourthTierAmount * 2L / 1000L
            val fifthTierAmount = maxOf(0L, minOf(balance - 50_000_000L, 110_000_000L))
            val fifthTierInterest = fifthTierAmount * 1L / 1000L

            firstTierInterest + secondTierInterest + thirdTierInterest + fourthTierInterest + fifthTierInterest
        },
        500_000L,
        160_000_000L,
        NamedTextColor.RED,
    ),
    LUXURIOUS(
        5_000_000_000L,
        { balance ->
            // First 10M yields 2% interest, 10M-20M yields 1% interest
            // 20M-30M yields 0.5% interest, 30M-50M yields 0.2% interest
            // 50M-160M yields 0.1% interest, 160M-5.16B yields 0.01% interest
            val firstTierInterest = minOf(balance, 10_000_000L) * 2L / 100L
            val secondTierAmount = maxOf(0L, minOf(balance - 10_000_000L, 10_000_000L))
            val secondTierInterest = secondTierAmount * 1L / 100L
            val thirdTierAmount = maxOf(0L, minOf(balance - 20_000_000L, 10_000_000L))
            val thirdTierInterest = thirdTierAmount * 5L / 1000L
            val fourthTierAmount = maxOf(0L, minOf(balance - 30_000_000L, 20_000_000L))
            val fourthTierInterest = fourthTierAmount * 2L / 1000L
            val fifthTierAmount = maxOf(0L, minOf(balance - 50_000_000L, 110_000_000L))
            val fifthTierInterest = fifthTierAmount * 1L / 1000L
            val sixthTierAmount = maxOf(0L, minOf(balance - 160_000_000L, 5_000_000_000L))
            val sixthTierInterest = sixthTierAmount * 1 / 10_000L

            firstTierInterest + secondTierInterest + thirdTierInterest + fourthTierInterest + fifthTierInterest +
                sixthTierInterest
        },
        1_000_000L,
        5_160_000_000L,
        NamedTextColor.DARK_AQUA,
    ),
    PALATIAL(
        10_000_000_000L,
        { balance ->
            // First 10M yields 2% interest, 10M-20M yields 1% interest
            // 20M-30M yields 0.5% interest, 30M-50M yields 0.2% interest
            // 50M-160M yields 0.1% interest, 160M-5.1B yields 0.01% interest
            // 5.16B to 55.16B yields 0.001% interest
            val firstTierInterest = minOf(balance, 10_000_000L) * 2L / 100L
            val secondTierAmount = maxOf(0L, minOf(balance - 10_000_000L, 10_000_000L))
            val secondTierInterest = secondTierAmount * 1L / 100L
            val thirdTierAmount = maxOf(0L, minOf(balance - 20_000_000L, 10_000_000L))
            val thirdTierInterest = thirdTierAmount * 5L / 1000L
            val fourthTierAmount = maxOf(0L, minOf(balance - 30_000_000L, 20_000_000L))
            val fourthTierInterest = fourthTierAmount * 2L / 1000L
            val fifthTierAmount = maxOf(0L, minOf(balance - 50_000_000L, 110_000_000L))
            val fifthTierInterest = fifthTierAmount * 1L / 1000L
            val sixthTierAmount = maxOf(0L, minOf(balance - 160_000_000L, 5_000_000_000L))
            val sixthTierInterest = sixthTierAmount * 1 / 10_000L
            val seventhTierAmount = maxOf(0L, minOf(balance - 5_160_000_000L, 50_000_000_000L))
            val seventhTierInterest = seventhTierAmount * 1 / 100_000L

            firstTierInterest + secondTierInterest + thirdTierInterest + fourthTierInterest + fifthTierInterest +
                sixthTierInterest + seventhTierInterest
        },
        1_500_000L,
        55_160_000_000L,
        NamedTextColor.DARK_RED,
    ),
}

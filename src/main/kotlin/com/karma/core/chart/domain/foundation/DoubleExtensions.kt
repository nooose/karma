package com.karma.core.chart.domain.foundation

fun Double.percentage(round: Int): Double {
    return this.percentage.roundTo(round)
}

fun Double.roundTo(decimals: Int): Double {
    return "%.${decimals}f".format(this).toDouble()
}

val Double.percentage: Double
    get() {
        return this * 100.0
    }

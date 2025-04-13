package com.karma.core.chart.domain

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * 봉 목록 도메인 모델
 */
data class Candles(
    val coinSymbol: CoinSymbol,
    val interval: Duration,
    private val _values: List<Candle>,
) {
    val values: List<Candle> = _values.sortedDescending()
    val latest: Candle
        get() = this.values.first()
    val withoutLatest: Candles
        get() = this.copy(_values = this.values.drop(1))
    val isEmpty: Boolean
        get() = this.values.isEmpty()
    val size: Int
        get() = this.values.size

    fun isSatisfied(strategy: CandleStrategy): Boolean {
        return strategy.isSatisfied(values)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Candles

        if (coinSymbol != other.coinSymbol) return false
        if (interval != other.interval) return false
        if (values != other.values) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coinSymbol.hashCode()
        result = 31 * result + interval.hashCode()
        result = 31 * result + values.hashCode()
        return result
    }

    override fun toString(): String {
        return "[$coinSymbol-$interval]$values"
    }

    companion object {
        val EMPTY = Candles(
            coinSymbol = CoinSymbol.UNKNOWN,
            interval = 0.toDuration(DurationUnit.MINUTES),
            _values = emptyList(),
        )
    }
}

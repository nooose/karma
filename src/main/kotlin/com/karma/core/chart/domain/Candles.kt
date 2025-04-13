package com.karma.core.chart.domain

import java.time.LocalDateTime
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
    val values: List<Candle> = _values.sortedByDescending { it.createdAt }
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

    fun containsCreatedAt(createdAt: LocalDateTime): Boolean {
        return values.map { it.createdAt }.contains(createdAt)
    }

    override fun toString(): String {
        return "[$interval]$values"
    }

    companion object {
        val EMPTY = Candles(
            coinSymbol = CoinSymbol.UNKNOWN,
            interval = 0.toDuration(DurationUnit.MINUTES),
            _values = emptyList(),
        )
    }
}

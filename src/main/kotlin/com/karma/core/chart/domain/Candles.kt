package com.karma.core.chart.domain

import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * 봉 목록 도메인 모델
 */
class Candles(
    val interval: Duration,
    values: List<Candle>,
) {
    val values: List<Candle> = values.sortedByDescending { it.createdAt }
    val latest: Candle
        get() = this.values.first()
    val withoutLatest: Candles
        get() = Candles(this.interval, this.values.drop(1))
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Candles

        return values == other.values
    }

    override fun hashCode(): Int {
        return values.hashCode()
    }

    override fun toString(): String {
        return "[$interval]$values"
    }

    companion object {
        val EMPTY = Candles(0.toDuration(DurationUnit.MINUTES), emptyList())
    }
}

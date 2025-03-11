package com.karma.core.chart.domain

import java.time.LocalDateTime

/**
 * 봉 목록 도메인 모델
 */
class Candles(
    values: List<Candle>,
) {
    val values: List<Candle> = values.sortedByDescending { it.createdAt }
    val latest: Candle
        get() = this.values.first()
    val withoutLatest: Candles
        get() = Candles(this.values.drop(1))
    val isEmpty: Boolean
        get() = this.values.isEmpty()

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
        return "$values"
    }

    companion object {
        val EMPTY = Candles(emptyList())
    }
}

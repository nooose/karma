package com.karma.core.chart.domain

/**
 * 봉 판단 전략
 */
interface CandleStrategy {
    fun isSatisfied(candles: List<Candle>): Boolean
}
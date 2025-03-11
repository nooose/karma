package com.karma.core.domain

/**
 * 봉 판단 전략
 */
interface CandleStrategy {
    fun isSatisfied(candles: List<Candle>): Boolean
}
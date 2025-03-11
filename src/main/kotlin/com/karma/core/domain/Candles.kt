package com.karma.core.domain

/**
 * 봉 목록 도메인 모델
 */
data class Candles(
    val values: List<Candle>,
) {

    fun isSatisfied(strategy: CandleStrategy): Boolean {
        return strategy.isSatisfied(values)
    }
}

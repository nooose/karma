package com.karma.core.chart.domain

import org.springframework.stereotype.Component

/**
 * 최신 연속 하락 봉을 판단하는 전략
 */
@Component
class LatestDownCandlesStrategy(
    private val count: Int = 2,
    override val title: String = "최신 $count 하락",
) : CandleStrategy {

    override fun isSatisfied(candles: List<Candle>): Boolean {
        if (candles.size < count) return false
        return candles.take(count).all { it.isDown }
    }
}

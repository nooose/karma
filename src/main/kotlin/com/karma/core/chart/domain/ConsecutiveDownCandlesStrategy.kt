package com.karma.core.chart.domain

import org.springframework.stereotype.Component

/**
 * 연속 하락 봉을 판단하는 전략
 */
@Component
class ConsecutiveDownCandlesStrategy(
    private val consecutiveCount: Int = 3
) : CandleStrategy {
    override fun isSatisfied(candles: List<Candle>): Boolean {
        return candles
            .windowed(consecutiveCount)
            .any { window ->
                window.all { it.isDown }
            }
    }
}

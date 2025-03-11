package com.karma.core.domain

/**
 * 연속 하락 봉을 판단하는 전략
 */
class ConsecutiveDownCandlesStrategy(
    private val consecutiveCount: Int
) : CandleStrategy {
    override fun isSatisfied(candles: List<Candle>): Boolean {
        return candles
            .windowed(consecutiveCount)
            .any { window -> 
                window.all { it.isDown }
            }
    }
}

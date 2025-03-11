package com.karma.core.chart.domain

import java.time.LocalDateTime
import kotlin.math.abs

/**
 * 봉 도메인 클래스
 */
data class Candle(
    val openPrice: Double,
    val closePrice: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val createdAt: LocalDateTime,
) {

    val isUp: Boolean get() = openPrice < closePrice
    val isDown: Boolean get() = openPrice > closePrice
    val isDoji:  Boolean get() = openPrice == closePrice
    val diff = abs(openPrice - closePrice)
}

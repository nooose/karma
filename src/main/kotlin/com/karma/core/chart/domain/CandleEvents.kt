package com.karma.core.chart.domain

import kotlin.time.Duration

/**
 * 매수 시점을 감지하는 이벤트
 */
data class BuySignalEvent(
    val message: String,
)

/**
 * 봉 갱신이 완료되었을 때 이벤트
 */
data class CandleRefreshedEvent(
    val symbol: CoinSymbol,
    val interval: Duration,
)

package com.karma.core.chart.domain

import kotlin.time.Duration

/**
 * 봉 저장소
 */
interface CandleRepository {

    fun refresh(candles: Candles)
    fun getLatest(symbol: CoinSymbol, interval: Duration): Candles
}

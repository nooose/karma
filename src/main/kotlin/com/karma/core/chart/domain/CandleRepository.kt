package com.karma.core.chart.domain

/**
 * 봉 저장소
 */
interface CandleRepository {

    fun refresh(candles: Candles)
    fun getLatest(): Candles
}
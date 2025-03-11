package com.karma.core.chart.domain

import kotlin.time.Duration

/**
 * 봉 데이터 외부 시스템 통신을 담당
 */
interface CandleClient {

    /**
     * 실시간 봉 목록 조회
     *
     * @param symbol 심볼
     * @param interval 봉 간격
     * @param limit 조회 갯수
     * @return 봉 목록
     */
    fun getCandles(symbol: String, interval: Duration, limit: Int? = null): Candles
}
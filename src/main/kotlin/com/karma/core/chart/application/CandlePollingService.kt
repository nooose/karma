package com.karma.core.chart.application

import com.karma.core.chart.domain.CandleClient
import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.foundation.BATCH_INTERVAL_MS
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class CandlePollingService(
    private val candleClient: CandleClient,
    private val candleRepository: CandleRepository,
) {

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = BATCH_INTERVAL_MS)
    suspend fun run() {
        val candles = getCandles()
        candleRepository.refresh(candles)
    }

    private fun getCandles(): Candles {
        try {
            return candleClient.getCandles(COIN_NAME, CANDLE_INTERVAL)
        } catch (e: Exception) {
            log.error { "봉 목록 조회에 실패하였습니다." }
            return Candles.EMPTY
        }
    }

    companion object {
        private const val COIN_NAME = "XRP"
        private val CANDLE_INTERVAL = 5.toDuration(DurationUnit.MINUTES)
    }
}
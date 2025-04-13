package com.karma.core.chart.application.bit

import com.karma.core.chart.application.BATCH_INTERVAL_MS
import com.karma.core.chart.domain.CandleClient
import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.domain.CoinSymbol
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * 봉 갱신 배치 서비스
 */
@Service
class BitCandlePollingService(
    private val candleClient: CandleClient,
    private val candleRepository: CandleRepository,
) {

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = BATCH_INTERVAL_MS)
    fun run() {
        for (interval in INTERVALS) {
            val candles = getCandles(interval)
            candleRepository.refresh(candles)
        }
    }

    private fun getCandles(interval: Duration): Candles {
        try {
            return candleClient.getCandles(CoinSymbol.BIT, interval)
        } catch (e: Exception) {
            log.error(e) { "봉 목록 조회에 실패하였습니다." }
            return Candles.Companion.EMPTY
        }
    }

    companion object {
        val INTERVALS = listOf(5, 15).map { it.toDuration(DurationUnit.MINUTES) }
    }
}

package com.karma.core.chart.application.bit

import com.karma.core.chart.application.INTERVAL_15M
import com.karma.core.chart.application.INTERVAL_5M
import com.karma.core.chart.domain.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import kotlin.time.Duration

/**
 * 비트코인 봉 갱신 배치 서비스
 */
class BitCandleRefresher(
    private val candleClient: CandleClient,
    private val candleRepository: CandleRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    private val log = KotlinLogging.logger {}

    @Scheduled(cron = "\${karma.refresh.five-cron}")
    fun run5m() {
        val candles = getCandles(INTERVAL_5M)
        candleRepository.refresh(candles)
        log.info { "$candles 갱신 완료" }

        val event = CandleRefreshedEvent(
            symbol = CoinSymbol.BIT,
            interval = INTERVAL_5M,
        )
        eventPublisher.publishEvent(event)
    }

    @Scheduled(cron = "\${karma.refresh.fifteen-cron}")
    fun run15m() {
        val candles = getCandles(INTERVAL_15M)
        candleRepository.refresh(candles)
        log.info { "$candles 갱신 완료" }

        val event = CandleRefreshedEvent(
            symbol = CoinSymbol.BIT,
            interval = INTERVAL_15M,
        )
        eventPublisher.publishEvent(event)
    }

    private fun getCandles(interval: Duration): Candles {
        try {
            return candleClient.getCandles(symbol = CoinSymbol.BIT, interval = interval)
        } catch (e: Exception) {
            log.error(e) { "봉 목록 조회에 실패하였습니다." }
            return Candles.Companion.EMPTY
        }
    }
}

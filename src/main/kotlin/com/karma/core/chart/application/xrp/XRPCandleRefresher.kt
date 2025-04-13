package com.karma.core.chart.application.xrp

import com.karma.core.chart.application.INTERVAL_15M
import com.karma.core.chart.application.INTERVAL_5M
import com.karma.core.chart.domain.CandleClient
import com.karma.core.chart.domain.CandleRefreshedEvent
import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.domain.CoinSymbol
import com.karma.core.chart.domain.CoinSymbol.XRP
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import kotlin.time.Duration

/**
 * XRP 봉 갱신 배치 서비스
 */
@Service
class XRPCandleRefresher(
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
            symbol = XRP,
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
            symbol = XRP,
            interval = INTERVAL_15M,
        )
        eventPublisher.publishEvent(event)
    }

    private fun getCandles(interval: Duration): Candles {
        try {
            return candleClient.getCandles(symbol = XRP, interval)
        } catch (e: Exception) {
            log.error(e) { "봉 목록 조회에 실패하였습니다." }
            return Candles.Companion.EMPTY
        }
    }
}

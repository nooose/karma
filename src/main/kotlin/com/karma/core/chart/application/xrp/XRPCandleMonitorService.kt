package com.karma.core.chart.application.xrp

import com.karma.core.chart.application.BATCH_INTERVAL_MS
import com.karma.core.chart.application.bit.BitCandlePollingService
import com.karma.core.chart.domain.BuySignalEvent
import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.CandleStrategy
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.domain.CoinSymbol
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.time.Duration

/**
 * 봉 모니터링 배치 서비스
 */
@Component
class XRPCandleMonitorService(
    private val candleRepository: CandleRepository,
    @Qualifier("consecutiveDownCandlesStrategy")
    private val consecutiveDownStrategy: CandleStrategy,
    @Qualifier("latestDownCandlesStrategy")
    private val latestDownStrategy: CandleStrategy,
    private val eventPublisher: ApplicationEventPublisher,
) {
    private val lastCompletedAtMap : MutableMap<Duration, LocalDateTime> = mutableMapOf()

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = BATCH_INTERVAL_MS)
    fun monitorCandles() {
        val candleData = BitCandlePollingService.Companion.INTERVALS.associateWith {
            candleRepository.getLatest(CoinSymbol.XRP, it)
        }

        val candles5m = candleData[BitCandlePollingService.Companion.INTERVALS[0]] ?: return
        val candles15m = candleData[BitCandlePollingService.Companion.INTERVALS[1]] ?: return

        val satisfied5m = monitor(candles5m)
        monitor(candles15m)

        if (satisfied5m && candles15m.isSatisfied(latestDownStrategy)) {
            val message = "⚠️ 5분봉 감지! 하지만 15분봉이 약세 흐름, 추세를 지켜보세요."
            eventPublisher.publishEvent(BuySignalEvent(message))
        }
    }

    private fun monitor(candles: Candles): Boolean {
        if (shouldSkip(candles)) {
            return false
        }

        markAsCompleted(candles)
        val message = makeMessage(candles)
        eventPublisher.publishEvent(BuySignalEvent(message))
        return true
    }

    private fun shouldSkip(candles: Candles): Boolean {
        val lastCompletedAt = lastCompletedAtMap[candles.interval] ?: LocalDateTime.MIN
        if (candles.isEmpty || candles.containsCreatedAt(lastCompletedAt)) {
            return true
        }

        val withoutLatest = candles.withoutLatest
        return !withoutLatest.isSatisfied(consecutiveDownStrategy)
    }

    private fun markAsCompleted(candles: Candles) {
        lastCompletedAtMap[candles.interval] = candles.latest.createdAt
    }

    private fun makeMessage(candles: Candles): String {
        val openPrice = candles.latest.openPrice
        val interval = candles.interval

        val strategyTitle = consecutiveDownStrategy.title
        return when (interval) {
            BitCandlePollingService.Companion.INTERVALS[0] -> "$strategyTitle-$interval 발생, 현재 시가: $openPrice"
            BitCandlePollingService.Companion.INTERVALS[1] -> "🎆 $strategyTitle-$interval 발생, 현재 시가: $openPrice"
            else -> "$strategyTitle 발생, 현재 시가: $openPrice"
        }
    }
}

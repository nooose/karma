package com.karma.core.chart.application

import com.karma.core.chart.domain.BuySignalEvent
import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.CandleStrategy
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.foundation.BATCH_INTERVAL_MS
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit.MINUTES
import kotlin.time.toDuration

@Component
class CandleMonitorService(
    private val candleRepository: CandleRepository,
    private val strategy: CandleStrategy,
    private val eventPublisher: ApplicationEventPublisher,
) {
    private val lastCompletedAtMap : MutableMap<Duration, LocalDateTime> = mutableMapOf()

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = BATCH_INTERVAL_MS)
    suspend fun monitorCandles() {
        INTERVALS.forEach { interval ->
            val candles = candleRepository.getLatest(interval)
            monitor(candles)
        }
    }

    private fun monitor(candles: Candles) {
        if (shouldSkip(candles)) {
            return
        }

        val latestCandle = candles.latest
        markAsCompleted(candles)
        val message = "${this.strategy.title}-${candles.interval} 발생, 현재 시가: ${latestCandle.openPrice}"
        eventPublisher.publishEvent(BuySignalEvent(message))
    }

    private fun shouldSkip(candles: Candles): Boolean {
        val lastCompletedAt = lastCompletedAtMap[candles.interval] ?: LocalDateTime.MIN
        if (candles.isEmpty || candles.containsCreatedAt(lastCompletedAt)) {
            return true
        }

        val withoutLatest = candles.withoutLatest
        return !withoutLatest.isSatisfied(strategy)
    }

    private fun markAsCompleted(candles: Candles) {
        lastCompletedAtMap[candles.interval] = candles.latest.createdAt
    }

    private fun makeMessage(candles: Candles): String {
        val openPrice = candles.latest.openPrice
        val interval = candles.interval

        if (interval.inWholeMinutes == 5L) {
            return "${this.strategy.title}-$interval 발생, 현재 시가: $openPrice"
        }

        if (interval.inWholeMinutes == 15L) {
            return "🎆 ${this.strategy.title}-$interval 발생, 현재 시가: $openPrice"
        }

        return "${this.strategy.title} 발생, 현재 시가: $openPrice"
    }

    companion object {
        private val INTERVALS = listOf(5, 15).map { it.toDuration(MINUTES) }
    }
}
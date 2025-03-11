package com.karma.core.chart.application

import com.karma.core.chart.domain.*
import com.karma.core.chart.foundation.BATCH_INTERVAL_MS
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CandleMonitorService(
    private val candleRepository: CandleRepository,
    private val messageSender: CandleMessageSender,
    private val strategy: CandleStrategy,
) {
    private var lastCompletedAt : LocalDateTime = LocalDateTime.MIN

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = BATCH_INTERVAL_MS)
    suspend fun monitorCandles() {
        val candles: Candles = candleRepository.getLatest()
        if (shouldSkip(candles)) {
            return
        }

        val latestCandle = candles.latest
        markAsCompleted(latestCandle)
        val message = makeNotificationMessage(latestCandle)
        notify(message)
    }

    private fun shouldSkip(candles: Candles): Boolean {
        if (candles.isEmpty || candles.containsCreatedAt(lastCompletedAt)) {
            return true
        }

        val withoutLatest = candles.withoutLatest
        return !withoutLatest.isSatisfied(strategy)
    }

    private fun markAsCompleted(candle: Candle) {
        this.lastCompletedAt = candle.createdAt
    }

    private fun makeNotificationMessage(candle: Candle): String {
        val tag = when (strategy) {
            is ConsecutiveDownCandlesStrategy -> "하락 봉"
            else -> ""
        }

        return "[$tag] 발생, 현재 시가: ${candle.openPrice}"
    }

    private fun notify(message: String) {
        try {
            messageSender.send(message)
        } catch (e: Exception) {
            log.error { "알람 메시지 전송에 실패하였습니다." }
        }
    }
}
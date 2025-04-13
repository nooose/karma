package com.karma.core.chart.application

import com.karma.core.chart.domain.BuySignalEvent
import com.karma.core.chart.domain.CandleRefreshedEvent
import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.CandleStrategy
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.domain.CoinSymbol
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import kotlin.time.toJavaDuration

/**
 * 매수 판단 배치 서비스
 */
@Service
class CandleChecker(
    private val strategy: CandleStrategy,
    private val repository: CandleRepository,
    private val formatter: CandlesAlertFormatter,
    private val eventPublisher: ApplicationEventPublisher,
) {

    private val log = KotlinLogging.logger {}

    @Async
    @EventListener
    fun handle(event: CandleRefreshedEvent) {
        val candles = repository.getLatest(event.symbol, event.interval)
        val withoutLatest = candles.withoutLatest
        if (!withoutLatest.isSatisfied(strategy)) {
            return
        }

        val message = formatter.format(candles = candles)
        eventPublisher.publishEvent(BuySignalEvent(message = message))
    }
}

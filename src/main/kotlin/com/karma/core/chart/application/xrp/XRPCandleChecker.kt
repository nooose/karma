package com.karma.core.chart.application.xrp

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

/**
 * XRP 매수 판단 배치 서비스
 */
@Service
class XRPCandleChecker(
    @Qualifier("consecutiveDownCandlesStrategy")
    private val consecutiveDownStrategy: CandleStrategy,
    private val repository: CandleRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    private val log = KotlinLogging.logger {}

    @Async
    @EventListener(condition = "#event.symbol == 'XRP'")
    fun handle(event: CandleRefreshedEvent) {
        log.info { "$event 갱신 완료" }
        val candles = repository.getLatest(CoinSymbol.XRP, event.interval)

        if (!candles.isSatisfied(consecutiveDownStrategy)) {
            return
        }

        val message = makeMessage(candles)
        eventPublisher.publishEvent(BuySignalEvent(message = message))
    }

    private fun makeMessage(candles: Candles): String {
        val openPrice = candles.latest.openPrice
        val interval = candles.interval

        val strategyTitle = consecutiveDownStrategy.title
        return "$strategyTitle-$interval 발생, 현재 시가: $openPrice"
    }
}

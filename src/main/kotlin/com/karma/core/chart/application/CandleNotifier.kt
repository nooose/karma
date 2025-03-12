package com.karma.core.chart.application

import com.karma.core.chart.domain.BuySignalEvent
import com.karma.core.chart.domain.CandleMessageSender
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class CandleNotifier(
    private val messageSender: CandleMessageSender,
) {

    private val log = KotlinLogging.logger {}


    @Async
    @EventListener
    fun handle(event: BuySignalEvent) {
        try {
            messageSender.send(event.message)
        } catch (e: Exception) {
            log.error { "알람 메시지 전송에 실패하였습니다. ${e.stackTraceToString()}" }
        }
    }
}
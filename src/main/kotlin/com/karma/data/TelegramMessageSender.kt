package com.karma.data

import com.karma.core.chart.domain.CandleMessageSender
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class TelegramMessageSender : CandleMessageSender {

    private val log = KotlinLogging.logger {}

    override fun send(message: String) {
        log.info { "\"$message\" 메시지 전송됨" }
    }
}
package com.karma.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.karma.core.chart.domain.CandleMessageSender
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class TelegramMessageSender(
    @Qualifier("telegramWebClient")
    private val client: WebClient,
    private val properties: TelegramProperties,
) : CandleMessageSender {

    private val log = KotlinLogging.logger {}

    override fun send(message: String) {
        val payload = Payload(
            chatId = properties.chatId,
            text = message,
        )

        client.post()
            .uri { builder ->
                builder.path("/bot${properties.botToken}/sendMessage")
                    .build()
            }
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(Unit::class.java)
            .doOnSuccess { log.info { "\"$message\" 메시지 전송됨" } }
            .doOnError { log.error(it) { "텔레그램 메시지 전송 실패" } }
            .subscribe()
    }

    data class Payload(
        @field:JsonProperty("chat_id")
        val chatId: String,
        val text: String,
        @field:JsonProperty("parse_mode")
        val mode: String = "Markdown",
    )
}
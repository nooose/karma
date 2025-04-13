package com.karma.data.client

import com.fasterxml.jackson.annotation.JsonProperty
import com.karma.core.chart.domain.CandleMessageSender
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class TelegramMessageSender(
    @Qualifier("telegramWebClient")
    private val client: RestClient,
    private val properties: TelegramProperties,
) : CandleMessageSender {

    private val log = KotlinLogging.logger {}

    override fun send(message: String) {
        val payload = Payload(
            chatId = properties.chatId,
            text = message,
        )

        val response = client.post()
            .uri { builder ->
                builder.path("/bot${properties.botToken}/sendMessage")
                    .build()
            }
            .body(payload)
            .retrieve()
            .toBodilessEntity()

        if (response.statusCode.isError) {
            log.error { "텔레그램 메시지 전송 실패" }
        }
    }

    data class Payload(
        @field:JsonProperty("chat_id")
        val chatId: String,
        val text: String,
        @field:JsonProperty("parse_mode")
        val mode: String = "Markdown",
    )
}

package com.karma.data

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration(proxyBeanMethods = false)
class TelegramConfig(
    private val telegramProperties: TelegramProperties,
) {

    @Bean
    fun telegramWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(telegramProperties.host)
            .build()
    }
}
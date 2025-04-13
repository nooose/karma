package com.karma.data.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration(proxyBeanMethods = false)
class TelegramConfig(
    private val telegramProperties: TelegramProperties,
) {

    @Bean
    fun telegramWebClient(): RestClient {
        return RestClient.builder()
            .baseUrl(telegramProperties.host)
            .build()
    }
}

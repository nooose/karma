package com.karma.data

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration(proxyBeanMethods = false)
class OKXConfig(
    private val okxProperties: OKXProperties,
) {

    @Bean
    fun okxWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(okxProperties.host)
            .build()
    }
}
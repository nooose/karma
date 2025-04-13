package com.karma.data.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration(proxyBeanMethods = false)
class OKXConfig(
    private val okxProperties: OKXProperties,
) {

    @Bean
    fun okxWebClient(): RestClient {
        return RestClient.builder()
            .baseUrl(okxProperties.host)
            .build()
    }
}

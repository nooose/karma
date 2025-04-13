package com.karma.data.client

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("karma.okx")
data class OKXProperties(
    val host: String,
)

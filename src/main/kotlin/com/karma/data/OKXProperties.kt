package com.karma.data

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("karma.okx")
data class OKXProperties(
    val host: String,
)

package com.karma.data

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("karma.telegram")
data class TelegramProperties(
    val host: String,
    val botToken: String,
    val chatId: String,
)

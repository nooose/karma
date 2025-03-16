package com.karma.data

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("karma.rule-reminder")
data class RuleReminderProperties(
    val enabled: Boolean = false,
)

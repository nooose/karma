package com.karma.support

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler


@Configuration(proxyBeanMethods = false)
class SchedulingConfig {

    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = 10
        scheduler.setThreadNamePrefix("scheduler-")
        scheduler.isDaemon = true
        return scheduler
    }
}
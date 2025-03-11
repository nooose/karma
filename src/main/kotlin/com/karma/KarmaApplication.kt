package com.karma

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class KarmaApplication

fun main(args: Array<String>) {
	runApplication<KarmaApplication>(*args)
}

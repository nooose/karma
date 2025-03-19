package com.karma

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class KarmaApplication

fun main(args: Array<String>) {
	runApplication<KarmaApplication>(*args)
}

package com.karma.core.chart.application

import com.karma.core.chart.domain.CandleMessageSender
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import kotlin.random.Random

@Component
class RuleReminder(
    private val messageSender: CandleMessageSender,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        Flux.just(Unit)
            .expand {
                val delay = Duration.ofHours(Random.nextLong(1, 3))
                Mono.delay(delay).thenReturn(Unit)
            }
            .doOnNext {
                val ruleIndex = Random.nextInt(0, RULES.size)
                messageSender.send(RULES[ruleIndex])
            }
            .subscribe()
    }

    companion object {
        private val RULES = listOf(
            "원칙을 지키자",
            "욕심을 부리지 말자",
            "한번 더 생각하자",
            "오버나이트는 자제하자",
            "급하게 결정하지 말자",
            "리스크를 관리하자",
        )
    }
}

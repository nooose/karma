package com.karma.core.chart.application

import com.karma.core.chart.domain.CandleMessageSender
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * 원칙 리마인더 배치 서비스
 */
@ConditionalOnProperty("karma.rule-reminder.enabled", havingValue = "true")
@Component
class RuleReminder(
    private val messageSender: CandleMessageSender,
) : ApplicationRunner {

    private val log = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments) {
        log.info { "룰 리마인더 실행" }
        CoroutineScope(Dispatchers.Default).launch {
            alertLoop()
        }
    }

    private suspend fun alertLoop() {
        while (true) {
            val nextDelayHours = if (Random.nextBoolean()) 1 else 3
            log.info { "다음 알림은 ${nextDelayHours}시간 후입니다."}

            delay(nextDelayHours.toDuration(DurationUnit.HOURS))

            val ruleIndex = Random.nextInt(0, RULES.size)
            messageSender.send(RULES[ruleIndex])
        }
    }

    companion object {
        private val RULES = listOf(
            "원칙을 지키자",
            "욕심을 부리지 말자",
            "한번 더 생각하자",
            "오버나이트는 자제하자",
            "급하게 결정하지 말자",
            "리스크를 관리하자",
            "한 봉에서 여러번의 거래를 하지 않는다",
            "내 자본금의 5%로 시작한다",
            "추격 매수는 절대 하지않는다",
            "기회는 다시 돌아온다",
            "숏 포지션을 잡지 말자",
        )
    }
}

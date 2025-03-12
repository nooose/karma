package com.karma.core.chart.application

import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.CandleStrategy
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.domain.candleFixture
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*
import org.springframework.context.ApplicationEventPublisher
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class CandleMonitorServiceTest : DescribeSpec({

    val candleRepository = mockk<CandleRepository>()
    val strategy = mockk<CandleStrategy>()
    val eventPublisher = mockk<ApplicationEventPublisher>()
    val interval = 0.toDuration(DurationUnit.MINUTES)

    val sut = CandleMonitorService(
        candleRepository = candleRepository,
        strategy = strategy,
        eventPublisher = eventPublisher,
    )

    context("알람 전송") {
        describe("봉 목록이 비어있으면") {
            every { candleRepository.getLatest(any()) } returns Candles.EMPTY

            sut.monitorCandles()

            it("알림을 보내지 않는다.") {
                verify(exactly = 0) { eventPublisher.publishEvent(any()) }
            }
        }

        describe("봉 전략이 거짓이라면") {
            val candles = Candles(
                interval = 0.toDuration(DurationUnit.MINUTES),
                listOf(
                    candleFixture(
                        openPrice = 100,
                        closePrice = 200
                    )
                )
            )
            every { candleRepository.getLatest(any()) } returns candles
            every { strategy.isSatisfied(any()) } returns false

            sut.monitorCandles()

            it("알림을 보내지 않는다.") {
                verify(exactly = 0) { eventPublisher.publishEvent(any()) }
            }
        }

        describe("봉 전략이 참이라면") {
            val candles = Candles(
                interval = interval,
                values = listOf(
                    candleFixture(
                        openPrice = 100,
                        closePrice = 200
                    )
                )
            )
            every { candleRepository.getLatest(any()) } returns candles
            every { strategy.isSatisfied(any()) } returns true
            every { strategy.title } returns "테스트"
            every { eventPublisher.publishEvent(any<Any>()) } just runs

            sut.monitorCandles()

            it("알림을 보낸다.") {
                verify(exactly = 1) { eventPublisher.publishEvent(any<Any>()) }
            }
        }
    }
})

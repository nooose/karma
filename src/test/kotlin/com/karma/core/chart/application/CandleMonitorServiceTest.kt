package com.karma.core.chart.application

import com.karma.core.chart.domain.*
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*
import org.springframework.context.ApplicationEventPublisher
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@DisplayName("모니터링 테스트")
class CandleMonitorServiceTest : DescribeSpec({

    val candleRepository = mockk<CandleRepository>()
    val consecutiveDownStrategy = mockk<CandleStrategy>()
    val latestDownStrategy = mockk<CandleStrategy>()
    val eventPublisher = mockk<ApplicationEventPublisher>()
    val interval = 0.toDuration(DurationUnit.MINUTES)

    val sut = CandleMonitorService(
        candleRepository = candleRepository,
        eventPublisher = eventPublisher,
        consecutiveDownStrategy = consecutiveDownStrategy,
        latestDownStrategy = latestDownStrategy,
    )
    every { consecutiveDownStrategy.title } returns "연속 하락"
    every { latestDownStrategy.title } returns "최신 하락"

    context("알람 전송") {
        describe("봉 목록이 비어있으면") {
            every { candleRepository.getLatest(any()) } returns Candles.EMPTY

            sut.monitorCandles()

            it("알림을 보내지 않는다.") {
                verify(exactly = 0) { eventPublisher.publishEvent(any<BuySignalEvent>()) }
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
            every { consecutiveDownStrategy.isSatisfied(any()) } returns false

            sut.monitorCandles()

            it("알림을 보내지 않는다.") {
                verify(exactly = 0) { eventPublisher.publishEvent(any<BuySignalEvent>()) }
            }
        }

        describe("연속 하락 전략이 참이면") {
            val candles = Candles(
                interval = interval,
                values = listOf(
                    candleFixture(
                        openPrice = 100,
                        closePrice = 200
                    )
                )
            )
            every { candleRepository.getLatest(any()) } returnsMany listOf(
                Candles(30.toDuration(DurationUnit.MINUTES), candles.values),
                Candles(35.toDuration(DurationUnit.MINUTES), candles.values)
            )
            every { consecutiveDownStrategy.isSatisfied(any()) } returns true
            every { latestDownStrategy.isSatisfied(any()) } returns false
            every { eventPublisher.publishEvent(any<BuySignalEvent>()) } just runs

            sut.monitorCandles()

            it("알림을 보낸다.") {
                verify(exactly = 2) { eventPublisher.publishEvent(any<BuySignalEvent>()) }
            }
        }

        describe("연속 하락 전략이 참이고, 최신 하락 전략도 참이면") {
            val candles = Candles(
                interval = interval,
                values = listOf(
                    candleFixture(
                        openPrice = 100,
                        closePrice = 200
                    )
                )
            )
            every { candleRepository.getLatest(any()) } returnsMany listOf(
                Candles(10.toDuration(DurationUnit.MINUTES), candles.values),
                Candles(25.toDuration(DurationUnit.MINUTES), candles.values)
            )
            every { consecutiveDownStrategy.isSatisfied(any()) } returns true
            every { latestDownStrategy.isSatisfied(any()) } returns true
            every { eventPublisher.publishEvent(any<BuySignalEvent>()) } just runs

            sut.monitorCandles()

            it("추가 알림이 전송된다.") {
                val message = "⚠️ 5분봉 감지! 하지만 15분봉이 약세 흐름, 추세를 지켜보세요."
                val event = BuySignalEvent(message)
                verify(exactly = 1) { eventPublisher.publishEvent(event) }
            }
        }
    }

    afterTest {
        clearMocks(eventPublisher)
    }
})

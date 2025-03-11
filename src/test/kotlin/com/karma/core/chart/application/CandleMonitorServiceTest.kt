package com.karma.core.chart.application

import com.karma.core.chart.domain.*
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CandleMonitorServiceTest : DescribeSpec({

    val messageSender = mockk<CandleMessageSender>(relaxed = true)
    val candleRepository = mockk<CandleRepository>()
    val strategy = mockk<CandleStrategy>()

    val sut = CandleMonitorService(
        candleRepository = candleRepository,
        messageSender = messageSender,
        strategy = strategy,
    )

    context("알람 전송") {
        describe("봉 목록이 비어있으면") {
            every { candleRepository.getLatest() } returns Candles.EMPTY

            sut.monitorCandles()

            it("알림을 보내지 않는다.") {
                verify(exactly = 0) { messageSender.send(any()) }
            }
        }

        describe("봉 전략이 거짓이라면") {
            val candles = Candles(
                listOf(
                    candleFixture(
                        openPrice = 100,
                        closePrice = 200
                    )
                )
            )
            every { candleRepository.getLatest() } returns candles
            every { strategy.isSatisfied(any()) } returns false

            sut.monitorCandles()

            it("알림을 보내지 않는다.") {
                verify(exactly = 0) { messageSender.send(any()) }
            }
        }

        describe("봉 전략이 참이라면") {
            val candles = Candles(
                listOf(
                    candleFixture(
                        openPrice = 100,
                        closePrice = 200
                    )
                )
            )
            every { candleRepository.getLatest() } returns candles
            every { strategy.isSatisfied(any()) } returns true

            sut.monitorCandles()

            it("알림을 보낸다.") {
                verify(exactly = 1) { messageSender.send(any()) }
            }
        }
    }
})

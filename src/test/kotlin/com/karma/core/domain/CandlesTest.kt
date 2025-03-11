package com.karma.core.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@DisplayName("봉 목록 모델 테스트")
class CandlesTest : StringSpec({

    "연속되는 음봉을 찾을 수 있다." {
        val strategy = ConsecutiveDownCandlesStrategy(3)

        val candles = Candles(
            listOf(
                createCandleFixture(
                    openPrice = 100,
                    closePrice = 90,
                ),
                createCandleFixture(
                    openPrice = 90,
                    closePrice = 80,
                ),
                createCandleFixture(
                    openPrice = 80,
                    closePrice = 70,
                )
            )
        )

        candles.isSatisfied(strategy) shouldBe true
    }

    "연속되는 음봉을 찾을 수 없다." {
        val strategy = ConsecutiveDownCandlesStrategy(3)

        val candles = Candles(
            listOf(
                createCandleFixture(
                    openPrice = 100,
                    closePrice = 90,
                ),
                createCandleFixture(
                    openPrice = 90,
                    closePrice = 80,
                ),
                createCandleFixture(
                    openPrice = 80,
                    closePrice = 90,
                )
            )
        )

        candles.isSatisfied(strategy) shouldBe false
    }


    "양봉이 포함된 봉 목록에서도 연속되는 음봉을 판단할 수 있다." {
        val strategy = ConsecutiveDownCandlesStrategy(3)

        val firstUpCandles = Candles(
            listOf(
                createCandleFixture(
                    openPrice = 70,
                    closePrice = 100,
                ),
                createCandleFixture(
                    openPrice = 100,
                    closePrice = 90,
                ),
                createCandleFixture(
                    openPrice = 90,
                    closePrice = 80,
                ),
                createCandleFixture(
                    openPrice = 80,
                    closePrice = 70,
                ),
            )
        )

        val middleUpCandles = Candles(
            listOf(
                createCandleFixture(
                    openPrice = 100,
                    closePrice = 90,
                ),
                createCandleFixture(
                    openPrice = 90,
                    closePrice = 80,
                ),
                createCandleFixture(
                    openPrice = 80,
                    closePrice = 100,
                ),
                createCandleFixture(
                    openPrice = 100,
                    closePrice = 90,
                ),
                createCandleFixture(
                    openPrice = 90,
                    closePrice = 85,
                ),
                createCandleFixture(
                    openPrice = 85,
                    closePrice = 80,
                ),
            )
        )

        val lastUpCandles = Candles(
            listOf(
                createCandleFixture(
                    openPrice = 100,
                    closePrice = 90,
                ),
                createCandleFixture(
                    openPrice = 90,
                    closePrice = 80,
                ),
                createCandleFixture(
                    openPrice = 80,
                    closePrice = 70,
                ),
                createCandleFixture(
                    openPrice = 70,
                    closePrice = 100,
                ),
            )
        )

        firstUpCandles.isSatisfied(strategy) shouldBe true
        middleUpCandles.isSatisfied(strategy) shouldBe true
        lastUpCandles.isSatisfied(strategy) shouldBe true
    }
})

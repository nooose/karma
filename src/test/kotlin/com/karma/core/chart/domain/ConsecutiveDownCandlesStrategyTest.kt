package com.karma.core.chart.domain

import io.kotest.assertions.assertSoftly
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@DisplayName("연속 음봉 전략 테스트")
class ConsecutiveDownCandlesStrategyTest : StringSpec({

    "연속되는 음봉을 찾을 수 있다." {
        val strategy = ConsecutiveDownCandlesStrategy(3)

        val candles = listOf(
            candleFixture(
                openPrice = 100,
                closePrice = 90,
            ),
            candleFixture(
                openPrice = 90,
                closePrice = 80,
            ),
            candleFixture(
                openPrice = 80,
                closePrice = 70,
            )
        )

        strategy.isSatisfied(candles) shouldBe true
    }

    "연속되는 음봉을 찾을 수 없다." {
        val strategy = ConsecutiveDownCandlesStrategy(3)

        val candles = listOf(
            candleFixture(
                openPrice = 100,
                closePrice = 90,
            ),
            candleFixture(
                openPrice = 90,
                closePrice = 80,
            ),
            candleFixture(
                openPrice = 80,
                closePrice = 90,
            )
        )

        strategy.isSatisfied(candles) shouldBe false
    }


    "양봉이 포함된 봉 목록에서도 연속되는 음봉을 판단할 수 있다." {
        val strategy = ConsecutiveDownCandlesStrategy(3)

        val firstUpCandles = listOf(
            candleFixture(
                openPrice = 70,
                closePrice = 100,
            ),
            candleFixture(
                openPrice = 100,
                closePrice = 90,
            ),
            candleFixture(
                openPrice = 90,
                closePrice = 80,
            ),
            candleFixture(
                openPrice = 80,
                closePrice = 70,
            ),
        )

        val middleUpCandles = listOf(
            candleFixture(
                openPrice = 100,
                closePrice = 90,
            ),
            candleFixture(
                openPrice = 90,
                closePrice = 80,
            ),
            candleFixture(
                openPrice = 80,
                closePrice = 100,
            ),
            candleFixture(
                openPrice = 100,
                closePrice = 90,
            ),
            candleFixture(
                openPrice = 90,
                closePrice = 85,
            ),
            candleFixture(
                openPrice = 85,
                closePrice = 80,
            ),
        )

        val lastUpCandles = listOf(
            candleFixture(
                openPrice = 100,
                closePrice = 90,
            ),
            candleFixture(
                openPrice = 90,
                closePrice = 80,
            ),
            candleFixture(
                openPrice = 80,
                closePrice = 70,
            ),
            candleFixture(
                openPrice = 70,
                closePrice = 100,
            ),
        )

        assertSoftly {
            strategy.isSatisfied(firstUpCandles) shouldBe true
            strategy.isSatisfied(middleUpCandles) shouldBe true
            strategy.isSatisfied(lastUpCandles) shouldBe true
        }
    }
})

package com.karma.core.chart.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@DisplayName("연속 음봉 전략 테스트")
class LastDownCandlesStrategyTest : StringSpec({

    val strategy = LatestDownCandlesStrategy(2)

    "최신 음봉 2개를 찾을 수 있다." {
        val candle1 = candleFixture(
            openPrice = 100,
            closePrice = 90,
        )
        val candle2 = candleFixture(
            openPrice = 90,
            closePrice = 80,
        )
        val candle3 = candleFixture(
            openPrice = 80,
            closePrice = 100,
        )

        val candles = listOf(
            candle1,
            candle2,
            candle3,
        )

        strategy.isSatisfied(candles) shouldBe true
    }

    "최신 음봉 2개가 아니라면 판단하지 않는다." {
        val candle1 = candleFixture(
            openPrice = 100,
            closePrice = 90,
        )
        val candle2 = candleFixture(
            openPrice = 90,
            closePrice = 80,
        )
        val candle3 = candleFixture(
            openPrice = 80,
            closePrice = 100,
        )

        val candles = listOf(
            candle3,
            candle1,
            candle2,
        )

        strategy.isSatisfied(candles) shouldBe false
    }
})

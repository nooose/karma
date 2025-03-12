package com.karma.core.chart.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDateTime

@DisplayName("봉 모델 테스트")
class CandleTest : StringSpec({

    "음봉인지 확인한다." {
        val candle = candleFixture(
            openPrice = 150,
            closePrice = 100,
        )

        candle.isDown.shouldBeTrue()
    }

    "양봉인지 확인한다." {
        val candle = candleFixture(
            openPrice = 100,
            closePrice = 150,
        )

        candle.isUp.shouldBeTrue()
    }

    "도지인지 확인한다." {
        val candle = candleFixture(
            openPrice = 100,
            closePrice = 100,
        )

        candle.isDoji.shouldBeTrue()
    }
})

fun candleFixture(
    openPrice: Int,
    closePrice: Int,
    highPrice: Double = 0.0,
    lowPrice: Double = 0.0,
    createdAt: LocalDateTime = LocalDateTime.now(),
) = Candle(
    openPrice = openPrice.toDouble(),
    closePrice = closePrice.toDouble(),
    highPrice = highPrice,
    lowPrice = lowPrice,
    createdAt = createdAt,
)

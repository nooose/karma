package com.karma.core.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDateTime

@DisplayName("봉 모델 테스트")
class CandleTest : StringSpec({

    "음봉인지 확인한다." {
        val candle = createCandleFixture(
            openPrice = 150,
            closePrice = 100,
        )

        candle.isDown.shouldBeTrue()
    }

    "양봉인지 확인한다." {
        val candle = createCandleFixture(
            openPrice = 100,
            closePrice = 150,
        )

        candle.isUp.shouldBeTrue()
    }

    "도지인지 확인한다." {
        val candle = createCandleFixture(
            openPrice = 100,
            closePrice = 100,
        )

        candle.isDoji.shouldBeTrue()
    }
})

fun createCandleFixture(
    openPrice: Int,
    closePrice: Int,
    highPrice: Double = 0.0,
    lowPrice: Double = 0.0,
) = Candle(
    openPrice = openPrice.toDouble(),
    closePrice = closePrice.toDouble(),
    highPrice = highPrice,
    lowPrice = lowPrice,
    dateTime = LocalDateTime.now()
)

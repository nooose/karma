package com.karma.core.chart.domain

import io.kotest.assertions.assertSoftly
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@DisplayName("봉 컬렉션 모델 테스트")
class CandlesTest : StringSpec({
    val baseTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0)
    val candle30m = candleFixture(
        openPrice = 200,
        closePrice = 50,
        createdAt = baseTime.plusMinutes(30),
    )

    val candle15m = candleFixture(
        openPrice = 200,
        closePrice = 50,
        createdAt = baseTime.plusMinutes(15),
    )

    val candle45m = candleFixture(
        openPrice = 200,
        closePrice = 50,
        createdAt = baseTime.plusMinutes(45),
    )

    "봉 생성 시간 내림차순으로 정렬된다." {
        val candles = candlesFixture(candle30m, candle15m, candle45m)

        candles shouldBe candlesFixture(candle45m, candle30m, candle15m)
    }

    "최신 봉과 최신 봉을 제외한 봉 목록을 가져올 수 있다." {
        val candles = candlesFixture(candle45m, candle30m, candle15m)

        assertSoftly {
            candles.latest shouldBe candle45m
            candles.withoutLatest shouldBe candlesFixture(candle30m, candle15m)
        }
    }
})

fun candlesFixture(vararg candles: Candle): Candles {
    return Candles(
        interval = 0.toDuration(DurationUnit.MINUTES),
        values = candles.toList(),
    )
}

package com.karma.data

import com.karma.core.chart.domain.Candle
import com.karma.core.chart.domain.CandleClient
import com.karma.core.chart.domain.Candles
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.time.Duration

@Component
class UpbitClient : CandleClient {
    override fun getCandles(symbol: String, interval: Duration, limit: Int?): Candles {
        return Candles(
            listOf(
                Candle(
                    openPrice = 3000.0,
                    closePrice = 3500.0,
                    highPrice = 3500.0,
                    lowPrice = 2900.0,
                    createdAt = LocalDateTime.now(),
                )
            )
        )
    }
}
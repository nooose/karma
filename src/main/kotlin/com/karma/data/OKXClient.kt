package com.karma.data

import com.karma.core.chart.domain.Candle
import com.karma.core.chart.domain.CandleClient
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.domain.CoinSymbol
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration

@Component
class OKXClient(
    @Qualifier("okxWebClient")
    private val client: RestClient,
) : CandleClient {

    private val log = KotlinLogging.logger {}

    override fun getCandles(symbol: CoinSymbol, interval: Duration, limit: Int?): Candles {
        val response: Map<*, *> = client.get()
            .uri { uriBuilder ->
                uriBuilder.path("/api/v5/market/candles")
                    .queryParam("category", "linear")
                    .queryParam("instId", symbol.okxSymbol)
                    .queryParam("bar", interval.toString())
                    .queryParam("limit", limit ?: 5)
                    .build()
            }
            .retrieve()
            .body(Map::class.java) ?: throw IllegalStateException("OKX 마켓 조회 API 요청을 처리할 수 없습니다.")

        val candles = response["data"] as List<*>

        return candles.map {
            it as List<*>
            Candle(
                createdAt = toLocalDateTime((it[0] as String).toLong()),
                openPrice = (it[1] as String).toDouble(),
                highPrice = (it[2] as String).toDouble(),
                lowPrice = (it[3] as String).toDouble(),
                closePrice = (it[4] as String).toDouble(),
            )
        }.let {
            Candles(
                coinSymbol = symbol,
                interval = interval,
                _values = it
            )
        }
    }

    private val CoinSymbol.okxSymbol: String
        get() {
            return when (this) {
                CoinSymbol.UNKNOWN -> "UNKNOWN"
                CoinSymbol.BIT -> "BTC-USDT-SWAP"
                CoinSymbol.XRP -> "XRP-USDT-SWAP"
            }
        }

    private fun toLocalDateTime(timeStamp: Long): LocalDateTime {
        return Instant.ofEpochMilli(timeStamp)
            .atZone(ZoneId.systemDefault()) // 시스템 기본 시간대 사용
            .toLocalDateTime()
    }
}

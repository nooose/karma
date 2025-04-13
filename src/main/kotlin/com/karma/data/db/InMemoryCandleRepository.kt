package com.karma.data.db

import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.Candles
import com.karma.core.chart.domain.CoinSymbol
import org.springframework.stereotype.Repository
import kotlin.time.Duration

@Repository
class InMemoryCandleRepository : CandleRepository {

    private val bitCandleMap: MutableMap<Duration, Candles> = mutableMapOf()
    private val xrpCandleMap: MutableMap<Duration, Candles> = mutableMapOf()

    @Synchronized
    override fun refresh(candles: Candles) {
        when (candles.coinSymbol) {
            CoinSymbol.BIT -> this.bitCandleMap[candles.interval] = candles
            CoinSymbol.XRP -> this.xrpCandleMap[candles.interval] = candles
            else -> return
        }
    }

    @Synchronized
    override fun getLatest(coinSymbol: CoinSymbol, interval: Duration): Candles {
        return when (coinSymbol) {
            CoinSymbol.BIT -> this.bitCandleMap[interval]
            CoinSymbol.XRP -> this.xrpCandleMap[interval]
            else -> null
        } ?: Candles.Companion.EMPTY
    }
}

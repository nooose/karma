package com.karma.data

import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.Candles
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

@Repository
class InMemoryCandleRepository : CandleRepository {

    private val candleMap: MutableMap<Duration, Candles> = ConcurrentHashMap()

    override fun refresh(candles: Candles) {
        this.candleMap[candles.interval] = candles
    }

    override fun getLatest(interval: Duration): Candles {
        return this.candleMap[interval] ?: Candles.EMPTY
    }
}

package com.karma.data

import com.karma.core.chart.domain.CandleRepository
import com.karma.core.chart.domain.Candles
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicReference

@Repository
class InMemoryCandleRepository : CandleRepository {

    private val latest: AtomicReference<Candles> = AtomicReference(Candles.EMPTY)

    override fun refresh(candles: Candles) {
        this.latest.set(candles)
    }

    override fun getLatest(): Candles {
        return latest.get()
    }
}

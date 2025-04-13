package com.karma.core.chart.application

import com.karma.core.chart.domain.Candles
import com.karma.core.chart.domain.CoinSymbol
import org.springframework.stereotype.Component

@Component
class CandlesAlertFormatter {

    fun format(candles: Candles): String {
        val symbol = when (candles.coinSymbol) {
            CoinSymbol.UNKNOWN -> "Unknown"
            CoinSymbol.BIT -> "🟠BIT"
            CoinSymbol.XRP -> "⚫XRP"
        }

        val interval = candles.interval.inWholeMinutes
        return "🚨[$symbol] 매수 신호 - ${interval}분봉"
    }
}

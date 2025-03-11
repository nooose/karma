package com.karma.core.chart.domain

/**
 * 봉 관련 메시지를 외부 시스템으로 전송하는 인터페이스
 *
 * - 이 인터페이스는 봉 상태에 따른 알림을 외부 시스템(예: 메일, 푸시 알림, SMS 등)으로 전송하는 기능을 제공한다.
 */
interface CandleMessageSender {

    fun send(message: String)
}
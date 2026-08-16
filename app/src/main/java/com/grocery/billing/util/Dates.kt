package com.grocery.billing.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Dates {

    val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    fun now(): LocalDateTime = LocalDateTime.now()

    fun todayDateString(now: LocalDateTime = now()): String = now.format(DATE_FORMAT)

    fun timeString(now: LocalDateTime = now()): String = now.format(TIME_FORMAT)

    fun isoTimestamp(now: LocalDateTime = now()): String = now.toString()

    /** "dd/MM/yyyy" -> "dd MMM yyyy" for history list display (e.g. 16 Aug 2026). */
    fun shortDate(dayMonthYear: String): String {
        return try {
            LocalDate.parse(dayMonthYear, DATE_FORMAT)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        } catch (e: Exception) {
            dayMonthYear
        }
    }
}

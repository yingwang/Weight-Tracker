package com.weighttracker.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm", Locale.ENGLISH)
    private val shortDateFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH)

    fun formatDate(date: LocalDate): String = date.format(dateFormatter)
    fun formatDateTime(dateTime: LocalDateTime): String = dateTime.format(dateTimeFormatter)
    fun formatShortDate(date: LocalDate): String = date.format(shortDateFormatter)
    fun formatShortDateTime(dateTime: LocalDateTime): String = dateTime.format(shortDateFormatter)

    fun getStartOfWeek(): LocalDateTime {
        return LocalDate.now().minusDays(7).atStartOfDay()
    }

    fun getStartOfMonth(): LocalDateTime {
        return LocalDate.now().minusMonths(1).atStartOfDay()
    }

    fun getStartOfYear(): LocalDateTime {
        return LocalDate.now().minusYears(1).atStartOfDay()
    }

    fun daysBetween(start: LocalDate, end: LocalDate): Long {
        return ChronoUnit.DAYS.between(start, end)
    }
}

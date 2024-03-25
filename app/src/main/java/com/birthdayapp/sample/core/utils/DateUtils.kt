package com.birthdayapp.sample.core.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Date
import java.util.Locale

object DateUtils {

    private const val DATE_PATTERN = "yyyy-MM-dd"
    const val MONTHS_IN_ONE_YEAR = 12

    fun monthsFrom(date: Long): Int {
        val startDate = LocalDate.parse(
            SimpleDateFormat(
                DATE_PATTERN, Locale.getDefault()
            ).format(date)
        )
        val currentDate = LocalDate.parse(
            SimpleDateFormat(
                DATE_PATTERN, Locale.getDefault()
            ).format(Date())
        )
        val years = Period.between(startDate, currentDate).years
        val months = Period.between(startDate, currentDate).months

        return years * MONTHS_IN_ONE_YEAR + months
    }

    fun yearsFrom(date: Long): Int {
        val startDate =
            LocalDate.parse(
                SimpleDateFormat(
                    DATE_PATTERN, Locale.getDefault()
                ).format(date)
            )
        val currentDate = LocalDate.parse(
            SimpleDateFormat(
                DATE_PATTERN, Locale.getDefault()
            ).format(Date())
        )

        return Period.between(startDate, currentDate).years
    }
}

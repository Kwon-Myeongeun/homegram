package com.lovesme.homegram.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val DATE_YEAR_MONTH_DAY_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"

private const val DATE_TODAY_PATTERN = "yyyy-MM-dd"

class DateFormatText @Inject constructor() {
    private val currentLocale
        get() = SystemConfiguration.currentLocale

    private val cal = Calendar.getInstance()

    fun convertToDate(dateString: String): Date {
        return SimpleDateFormat(DATE_TODAY_PATTERN, currentLocale).parse(dateString)
            ?: Date()
    }

    fun getTodayYEAR(): Int {
        return cal.get(Calendar.YEAR)
    }

    fun getTodayMONTH(): Int {
        return cal.get(Calendar.MONTH)
    }

    fun getTodayDATE(): Int {
        return cal.get(Calendar.DATE)
    }

    fun getTodayText(): String {
        val formatter = SimpleDateFormat(DATE_TODAY_PATTERN, currentLocale)
        val currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
        return formatter.format(currentDate)
    }

    fun convertToDateText(year: Int, month: Int, date: Int): String {
        cal.set(year, month, date)
        return convertToNewDateFormat(
            DATE_TODAY_PATTERN,
            convertToDate("${year}-${month + 1}-${date}")
        )
    }

    fun convertToNewDateFormat(newPattern: String, date: Date): String {
        return SimpleDateFormat(newPattern, currentLocale).format(date)
    }

    fun getCurrentTimeText(): String {
        val formatter = SimpleDateFormat(DATE_YEAR_MONTH_DAY_TIME_PATTERN, currentLocale)
        val currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
        return formatter.format(currentDate)
    }
}
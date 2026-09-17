package com.example.spendwise.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val fullDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    private val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    private val dayMonthFormat = SimpleDateFormat("MMM d", Locale.getDefault())

    fun formatDate(timestamp: Long): String = fullDateFormat.format(Date(timestamp))

    fun formatMonthYear(timestamp: Long): String = monthYearFormat.format(Date(timestamp))

    fun formatTime(timestamp: Long): String = timeFormat.format(Date(timestamp))

    fun formatDayMonth(timestamp: Long): String = dayMonthFormat.format(Date(timestamp))

    fun formatDayOfWeek(timestamp: Long): String = dayOfWeekFormat.format(Date(timestamp))

    fun getRelativeDateHeader(timestamp: Long): String {
        val nowCal = Calendar.getInstance()
        val targetCal = Calendar.getInstance().apply { timeInMillis = timestamp }

        val isSameYear = nowCal.get(Calendar.YEAR) == targetCal.get(Calendar.YEAR)
        val nowDay = nowCal.get(Calendar.DAY_OF_YEAR)
        val targetDay = targetCal.get(Calendar.DAY_OF_YEAR)

        return when {
            isSameYear && nowDay == targetDay -> "Today"
            isSameYear && nowDay - targetDay == 1 -> "Yesterday"
            isSameYear -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
            else -> fullDateFormat.format(Date(timestamp))
        }
    }

    fun getStartOfMonth(calendar: Calendar = Calendar.getInstance()): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getEndOfMonth(calendar: Calendar = Calendar.getInstance()): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    fun getStartOfWeek(calendar: Calendar = Calendar.getInstance()): Long {
        val cal = calendar.clone() as Calendar
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}

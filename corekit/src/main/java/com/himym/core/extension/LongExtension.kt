package com.himym.core.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * 格式化文件大小。
 * @param locale 区域设置，用于国际化。
 * @param decimalPlaces 保留的小数位数。
 * @return 格式化后的文件大小字符串。
 */
fun Long.formatFileSize(locale: Locale = Locale.getDefault(), decimalPlaces: Int = 2): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var size = toDouble()
    var unit = 0

    while (size >= 1024 && unit < units.size - 1) {
        size /= 1024
        unit++
    }

    return String.format(locale, "%.${decimalPlaces}f %s", size, units[unit])
}

/**
 * 格式化时间戳为指定格式的日期字符串。
 * @param format 日期格式，默认为 "yyyy-MM-dd"。
 * @param locale 区域设置，默认为系统默认语言。
 * @param timeZone 时区，默认为系统默认时区。
 * @return 格式化后的日期字符串。
 */
fun Long.formatTimestamp(
    format: String = "yyyy-MM-dd",
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    return try {
        val sdf = SimpleDateFormat(format, locale)
        sdf.timeZone = timeZone
        sdf.format(this)
    } catch (e: Exception) {
        ""
    }
}

/**
 * 格式化持续时间为时分秒格式。
 * @param hourSeparator 小时的分隔符，默认为 ":"。
 * @param minSeparator 分钟的分隔符，默认为 ":"。
 * @param secondSeparator 秒的分隔符，默认为 ""。
 * @param includeMillis 是否包含毫秒，默认为 false。
 * @return 格式化后的持续时间字符串。
 */
@SuppressLint("DefaultLocale")
fun Long.formatDuration(
    hourSeparator: String = ":",
    minSeparator: String = ":",
    secondSeparator: String = "",
    includeMillis: Boolean = false
): String {
    if (this < 0) return "00${minSeparator}00${secondSeparator}00"

    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(hours)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)
    val millis = this % 1000
    return if (hours > 0) {
        if (includeMillis) String.format("%02d$hourSeparator%02d$minSeparator%02d$secondSeparator%03d", hours, minutes, seconds, millis)
        else String.format("%02d$hourSeparator%02d$minSeparator%02d$secondSeparator", hours, minutes, seconds)
    } else {
        if (includeMillis) String.format("%02d$minSeparator%02d$secondSeparator%03d", minutes, seconds, millis)
        else String.format("%02d$minSeparator%02d$secondSeparator", minutes, seconds)
    }
}
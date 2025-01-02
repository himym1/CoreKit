package com.topping.core.extension

import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import java.util.Base64

/**
 * @author himym.
 * @description 字符串扩展方法
 */

/** ========== 基础字符串操作 ========== */

fun String?.toNullIfEmpty(): String? = if (this.isNullOrEmpty()) null else this
fun String?.toNullIfBlank(): String? = if (this.isNullOrBlank()) null else this

fun String?.nullTo(defaultValue: String, checkBlank: Boolean = false): String =
    if ((checkBlank && this.isNullOrBlank()) || (!checkBlank && this.isNullOrEmpty())) {
        defaultValue
    } else {
        this ?: defaultValue
    }

fun String?.nullTo(defaultValue: () -> String, checkBlank: Boolean = false): String =
    if ((checkBlank && this.isNullOrBlank()) || (!checkBlank && this.isNullOrEmpty())) {
        defaultValue()
    } else {
        this ?: defaultValue()
    }

fun String.safeSubString(range: IntRange, adjustOutOfBounds: Boolean = false): String {
    if (!adjustOutOfBounds && (range.first !in 0..length || range.last !in 0..length)) {
        throw IllegalArgumentException("Range $range is out of bounds for string of length $length")
    }
    val start = range.first.coerceIn(0, length)
    val end = range.last.coerceIn(0, length)
    return substring(start, end + 1)
}

/** ========== 分组与格式化 ========== */
fun String.groupByIndex(
    groupSize: Int,
    separator: String = " ",
    maskMiddle: Boolean = false,
    maskChar: Char = '*'
): String = chunked(groupSize).mapIndexed { index, chunk ->
    if (maskMiddle && index != 0 && index != (this.length / groupSize)) {
        maskChar.toString().repeat(chunk.length)
    } else {
        chunk
    }
}.joinToString(separator)

/** ========== HTML 渲染与富文本 ========== */
fun String.renderHtml(flags: Int? = null): Spanned {
    return Html.fromHtml(this, flags ?: Html.FROM_HTML_MODE_LEGACY)
}

fun SpannableString.setSpanWithCustomStyle(
    start: Int,
    end: Int,
    styles: List<Any>,
    onClick: (() -> Unit)? = null
) {
    require(start in 0 until this.length && end in 0..this.length && start < end) {
        "Invalid range: start=$start, end=$end, length=${this.length}"
    }

    if (onClick != null) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick()
            }
        }
        setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    styles.forEach { style ->
        setSpan(style, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

/** ========== Base64 编解码 ========== */
fun ByteArray.encodeByBase64(): String = Base64.getEncoder().encodeToString(this)
fun String.decodeByBase64(): ByteArray = Base64.getDecoder().decode(this)
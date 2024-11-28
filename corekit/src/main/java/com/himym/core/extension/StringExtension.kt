package com.himym.core.extension

import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View

/**
 * @author himym.
 * @description
 */

fun String.renderHtml(flags: Int? = null): Spanned =
    Html.fromHtml(this, flags ?: Html.FROM_HTML_MODE_LEGACY)

fun SpannableString.setSpanWithClickable(
    start: Int,
    end: Int,
    color: Int,
    onClick: () -> Unit
) {
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick()
        }
    }
    this.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}
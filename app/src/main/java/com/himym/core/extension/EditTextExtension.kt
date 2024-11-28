package com.himym.core.extension

import android.widget.EditText

/**
 * @author himym.
 * @description
 */
fun EditText.clearText() {
    setText("")
}

fun EditText.checkValidate(msg: String = "", contentBlank: (String) -> Unit): String {
    val content = text.toString()
    if (content.isEmpty()) {
        contentBlank(msg.ifBlank { hint.toString() })
        return ""
    }

    return content
}
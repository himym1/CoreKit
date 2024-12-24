package com.himym.core.extension

import android.widget.EditText

/**
 * @author himym.
 * @description 文本框扩展方法
 */

/**
 *  清空文本框内容
 */
fun EditText.clearText() {
    setText("")
}

/**
 *  检查文本框内容是否为空
 */
fun EditText.checkValidate(msg: String = "", contentBlank: (String) -> Unit): String {
    val content = text.toString()
    if (content.isEmpty()) {
        contentBlank(msg.ifBlank { hint.toString() })
        return ""
    }

    return content
}
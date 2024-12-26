package com.himym.core.extension

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.himym.core.dialog.CustomListDialogFragment
import com.himym.core.dialog.InputDialogFragment
import com.himym.core.dialog.MessageDialogFragment
import com.himym.core.dialog.OptionSelectionDialogFragment
import com.himym.core.dialog.SingleChoiceDialogFragment

/**
 * @author himym
 * @description Dialog扩展
 */
fun FragmentManager.showMessageDialog(
    title: String,
    content: String,
    onConfirmClick: (() -> Unit)? = null
): MessageDialogFragment {

    return MessageDialogFragment.newInstance(title, content).apply {
        onConfirmClickListener = onConfirmClick
        show(this@showMessageDialog, "MessageDialog")
    }
}

/**
 * 扩展函数，便捷调用 InputDialogFragment
 * @param title 对话框标题
 * @param hint 输入框提示
 * @param onInputConfirmed 输入确定按钮点击回调，返回用户输入内容
 */
fun FragmentManager.showInputDialog(
    title: String,
    hint: String = "",
    onInputConfirmed: ((String) -> Unit)? = null
) {

    val dialog = InputDialogFragment.newInstance(title, hint)
    dialog.onInputConfirmed = onInputConfirmed
    dialog.showAllowStateLoss(this, "InputDialog")
}

/**
 * 扩展函数，便捷调用 OptionSelectionDialogFragment
 * @param title 对话框标题
 * @param options 选项列表
 * @param selectedIndex 默认选中项索引
 * @param onOptionSelected 回调函数，返回选中的索引和选项文本
 */
fun FragmentManager.showOptionSelectionDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int = -1,
    onOptionSelected: ((Int, String) -> Unit)? = null
) {

    val dialog = OptionSelectionDialogFragment.newInstance(title, options, selectedIndex)
    dialog.onOptionSelectedListener = onOptionSelected
    dialog.show(this, "OptionSelectionDialog")
}

/**
 * 扩展函数，便捷调用 OptionSelectionDialogFragment
 * @param title 对话框标题
 * @param options 选项列表
 * @param selectedIndex 默认选中项索引
 * @param onOptionSelected 回调函数，返回选中的索引和选项文本
 */
fun FragmentActivity.showOptionSelectionDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int = -1,
    onOptionSelected: ((Int, String) -> Unit)? = null
) {

    if (isFinishing || isDestroyed || supportFragmentManager.isStateSaved) {
        return
    }

    val dialog = OptionSelectionDialogFragment.newInstance(title, options, selectedIndex)
    dialog.onOptionSelectedListener = onOptionSelected
    dialog.showAllowStateLoss(supportFragmentManager, "OptionSelectionDialog")
}

/**
 * @author himym
 * @description Dialog扩展
 */
fun FragmentActivity.showMessageDialog(
    title: String,
    content: String,
    onConfirmClick: (() -> Unit)? = null
): MessageDialogFragment? {
    if (isFinishing || isDestroyed || supportFragmentManager.isStateSaved) {
        return null
    }

    val existingDialog = supportFragmentManager.findFragmentByTag("MessageDialog")
    if (existingDialog != null) {
        return existingDialog as? MessageDialogFragment
    }

    return MessageDialogFragment.newInstance(title, content).apply {
        onConfirmClickListener = onConfirmClick
        showAllowStateLoss(supportFragmentManager, "MessageDialog")
    }
}

/**
 * 扩展函数，便捷调用 InputDialogFragment
 * @param title 对话框标题
 * @param hint 输入框提示
 * @param onInputConfirmed 输入确定按钮点击回调，返回用户输入内容
 */
fun FragmentActivity.showInputDialog(
    title: String,
    hint: String = "",
    onInputConfirmed: ((String) -> Unit)? = null
) {

    if (isFinishing || isDestroyed || supportFragmentManager.isStateSaved) {
        return
    }

    val dialog = InputDialogFragment.newInstance(title, hint)
    dialog.onInputConfirmed = onInputConfirmed
    dialog.showAllowStateLoss(supportFragmentManager, "InputDialog")
}

fun FragmentManager.showSingleChoiceDialog(
    title: String,
    options: List<String>,
    onItemSelected: ((Int, String) -> Unit)? = null
) {
    val dialog = SingleChoiceDialogFragment.newInstance(title, options)
    dialog.onItemSelectedListener = onItemSelected
    dialog.show(this, "SingleChoiceDialog")
}

fun FragmentActivity.showSingleChoiceDialog(
    title: String,
    options: List<String>,
    onItemSelected: ((Int, String) -> Unit)? = null
) {
    if (isFinishing || isDestroyed || supportFragmentManager.isStateSaved) {
        return
    }

    val dialog = SingleChoiceDialogFragment.newInstance(title, options)
    dialog.onItemSelectedListener = onItemSelected
    dialog.showAllowStateLoss(supportFragmentManager, "SingleChoiceDialog")
}


fun <T> FragmentManager.showCustomListDialog(
    title: String,
    items: List<T>,
    @LayoutRes itemLayoutRes: Int,
    bindView: (View, T, Int) -> Unit,
    onItemSelected: ((Int, T) -> Unit)? = null
) {
    val dialog = CustomListDialogFragment.newInstance(
        title = title,
        items = items,
        itemLayoutRes = itemLayoutRes,
        bindView = bindView
    )
    dialog.onItemSelectedListener = onItemSelected
    dialog.show(this, "CustomListDialog")
}

fun <T> FragmentActivity.showCustomListDialog(
    title: String,
    items: List<T>,
    @LayoutRes itemLayoutRes: Int,
    bindView: (View, T, Int) -> Unit,
    onItemSelected: ((Int, T) -> Unit)? = null
) {
    if (isFinishing || isDestroyed || supportFragmentManager.isStateSaved) {
        return
    }

    val dialog = CustomListDialogFragment.newInstance(
        title = title,
        items = items,
        itemLayoutRes = itemLayoutRes,
        bindView = bindView
    )
    dialog.onItemSelectedListener = onItemSelected
    dialog.showAllowStateLoss(supportFragmentManager, "CustomListDialog")
}

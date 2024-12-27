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
import com.himym.core.dialog.SingleChoiceWithCheckDialogFragment

/**
 * @author himym
 * @description Dialog扩展函数集合，提供了一系列便捷的对话框显示方法
 */

/**
 * 显示消息对话框的FragmentManager扩展函数
 * @param title 对话框标题
 * @param content 对话框内容
 * @param onConfirmClick 确认按钮点击回调
 * @return 返回创建的MessageDialogFragment实例
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
 * 显示输入对话框的FragmentManager扩展函数
 * @param title 对话框标题
 * @param hint 输入框提示文本
 * @param onInputConfirmed 输入确认回调，参数为用户输入的文本
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
 * 显示选项选择对话框的FragmentManager扩展函数
 * @param title 对话框标题
 * @param options 选项列表
 * @param selectedIndex 默认选中项的索引，默认为-1表示没有选中项
 * @param onOptionSelected 选项选择回调，参数为选中项的索引和文本
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
 * 显示选项选择对话框的Activity扩展函数
 * @param title 对话框标题
 * @param options 选项列表
 * @param selectedIndex 默认选中项的索引，默认为-1表示没有选中项
 * @param onOptionSelected 选项选择回调，参数为选中项的索引和文本
 */
fun FragmentActivity.showOptionSelectionDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int = -1,
    onOptionSelected: ((Int, String) -> Unit)? = null
) {

    // 检查Activity状态，避免在Activity销毁后显示对话框
    if (isFinishing || isDestroyed || supportFragmentManager.isStateSaved) {
        return
    }

    val dialog = OptionSelectionDialogFragment.newInstance(title, options, selectedIndex)
    dialog.onOptionSelectedListener = onOptionSelected
    dialog.showAllowStateLoss(supportFragmentManager, "OptionSelectionDialog")
}

/**
 * 显示消息对话框的Activity扩展函数
 * @param title 对话框标题
 * @param content 对话框内容
 * @param onConfirmClick 确认按钮点击回调
 * @return 返回创建的MessageDialogFragment实例，如果Activity已销毁则返回null
 */
fun FragmentActivity.showMessageDialog(
    title: String,
    content: String,
    onConfirmClick: (() -> Unit)? = null
): MessageDialogFragment? {
    // 检查Activity状态
    if (isFinishing || isDestroyed || supportFragmentManager.isStateSaved) {
        return null
    }

    // 检查是否已存在相同的对话框
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
 * 显示输入对话框的Activity扩展函数
 * @param title 对话框标题
 * @param hint 输入框提示文本
 * @param onInputConfirmed 输入确认回调，参数为用户输入的文本
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

/**
 * 显示单选对话框的FragmentManager扩展函数
 * @param title 对话框标题
 * @param options 选项列表
 * @param onItemSelected 选项选择回调，参数为选中项的索引和文本
 */
fun FragmentManager.showSingleChoiceDialog(
    title: String,
    options: List<String>,
    onItemSelected: ((Int, String) -> Unit)? = null
) {
    val dialog = SingleChoiceDialogFragment.newInstance(title, options)
    dialog.onItemSelectedListener = onItemSelected
    dialog.show(this, "SingleChoiceDialog")
}

/**
 * 显示单选对话框的Activity扩展函数
 * @param title 对话框标题
 * @param options 选项列表
 * @param onItemSelected 选项选择回调，参数为选中项的索引和文本
 */
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

/**
 * 显示自定义列表对话框的FragmentManager扩展函数
 * @param title 对话框标题
 * @param items 列表数据项
 * @param itemLayoutRes 列表项布局资源ID
 * @param bindView 视图绑定回调，用于设置列表项的显示内容
 * @param onItemSelected 列表项选择回调，参数为选中项的索引和数据
 */
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

/**
 * 显示自定义列表对话框的Activity扩展函数
 * @param title 对话框标题
 * @param items 列表数据项
 * @param itemLayoutRes 列表项布局资源ID
 * @param bindView 视图绑定回调，用于设置列表项的显示内容
 * @param onItemSelected 列表项选择回调，参数为选中项的索引和数据
 */
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

/**
 * 显示带勾选标记的单选对话框的FragmentManager扩展函数
 * @param title 对话框标题
 * @param options 选项列表
 * @param checkedPosition 默认选中项的位置，默认为-1表示没有选中项
 * @param onItemSelected 选项选择回调，参数为选中项的索引和文本
 */
fun FragmentManager.showSingleChoiceWithCheckDialog(
    title: String,
    options: List<String>,
    checkedPosition: Int = -1,
    onItemSelected: ((Int, String) -> Unit)? = null
) {
    val dialog = SingleChoiceWithCheckDialogFragment.newInstance(title, options, checkedPosition)
    dialog.onItemSelectedListener = onItemSelected
    dialog.show(this, "SingleChoiceWithCheckDialog")
}

/**
 * 显示带勾选标记的单选对话框的Activity扩展函数
 * @param title 对话框标题
 * @param options 选项列表
 * @param checkedPosition 默认选中项的位置，默认为-1表示没有选中项
 * @param onItemSelected 选项选择回调，参数为选中项的索引和文本
 */
fun FragmentActivity.showSingleChoiceWithCheckDialog(
    title: String,
    options: List<String>,
    checkedPosition: Int = -1,
    onItemSelected: ((Int, String) -> Unit)? = null
) {
    if (isFinishing || isDestroyed || supportFragmentManager.isStateSaved) {
        return
    }

    val dialog = SingleChoiceWithCheckDialogFragment.newInstance(title, options, checkedPosition)
    dialog.onItemSelectedListener = onItemSelected
    dialog.showAllowStateLoss(supportFragmentManager, "SingleChoiceWithCheckDialog")
}
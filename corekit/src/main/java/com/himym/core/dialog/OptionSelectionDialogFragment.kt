package com.topping.core.dialog

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.os.bundleOf
import com.topping.core.Constants.ARG_OPTIONS
import com.topping.core.Constants.ARG_SELECTED_INDEX
import com.topping.core.Constants.ARG_TITLE
import com.topping.core.ui.BaseDialogFragment
import com.topping.corekit.databinding.DialogOptionSelectionBinding


/**
 * 通用的选项选择对话框
 */
class OptionSelectionDialogFragment : BaseDialogFragment<DialogOptionSelectionBinding>() {

    var onOptionSelectedListener: ((Int, String) -> Unit)? = null

    private var title: String? = null
    private var options: List<String> = emptyList()
    private var selectedIndex: Int = -1

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
        title = arguments?.getString(ARG_TITLE)
        options = arguments?.getStringArrayList(ARG_OPTIONS) ?: emptyList()
        selectedIndex = arguments?.getInt(ARG_SELECTED_INDEX, -1) ?: -1

        mBinding.apply {
            tvTitle.text = title

            // 动态添加选项到 RadioGroup
            options.forEachIndexed { index, option ->
                val radioButton = RadioButton(context)
                radioButton.id = index
                radioButton.text = option
                radioButton.isChecked = index == selectedIndex
                rgOptions.addView(radioButton)
            }

            // 确认按钮点击事件
            btnConfirm.setOnClickListener {
                val checkedRadioButtonId = rgOptions.checkedRadioButtonId
                if (checkedRadioButtonId != -1) {
                    val selectedOption = options[checkedRadioButtonId]
                    onOptionSelectedListener?.invoke(checkedRadioButtonId, selectedOption)
                    dismiss()
                }
            }

            // 取消按钮点击事件
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {

        // 创建实例，传入标题、选项列表和默认选中项的索引
        fun newInstance(title: String, options: List<String>, selectedIndex: Int = -1): OptionSelectionDialogFragment {
            return OptionSelectionDialogFragment().apply {
                arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_OPTIONS to ArrayList(options),
                    ARG_SELECTED_INDEX to selectedIndex
                )
            }
        }
    }
}
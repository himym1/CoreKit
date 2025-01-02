package com.topping.core.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import com.topping.core.Constants.ARG_OPTIONS
import com.topping.core.Constants.ARG_TITLE
import com.topping.core.ui.BaseDialogFragment
import com.topping.corekit.R
import com.topping.corekit.databinding.DialogSingleChoiceBinding

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/26
 * @desc: SingleChoiceDialogFragment
 */
class SingleChoiceDialogFragment : BaseDialogFragment<DialogSingleChoiceBinding>() {

    var onItemSelectedListener: ((Int, String) -> Unit)? = null

    private var title: String? = null
    private var options: List<String> = emptyList()

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
        title = arguments?.getString(ARG_TITLE)
        options = arguments?.getStringArrayList(ARG_OPTIONS) ?: emptyList()

        mBinding.apply {
            tvTitle.text = title

            // 动态添加选项
            options.forEachIndexed { index, option ->
                val itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_single_choice, llOptions, false)

                itemView.findViewById<TextView>(R.id.tv_option).text = option

                // 点击直接触发选择事件并关闭对话框
                itemView.setOnClickListener {
                    onItemSelectedListener?.invoke(index, option)
                    dismiss()
                }

                llOptions.addView(itemView)
            }
        }
    }

    companion object {
        fun newInstance(title: String, options: List<String>): SingleChoiceDialogFragment {
            return SingleChoiceDialogFragment().apply {
                arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_OPTIONS to ArrayList(options)
                )
            }
        }
    }
}
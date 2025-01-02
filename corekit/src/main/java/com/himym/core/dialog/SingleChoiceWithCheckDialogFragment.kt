package com.himym.core.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import com.himym.core.Constants.ARG_OPTIONS
import com.himym.core.Constants.ARG_TITLE
import com.himym.core.ui.BaseDialogFragment
import com.himym.corekit.R
import com.himym.corekit.databinding.DialogSingleChoiceBinding

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/27
 * @desc: 单选对话框，带有选中状态图标
 */
class SingleChoiceWithCheckDialogFragment : BaseDialogFragment<DialogSingleChoiceBinding>() {
    var onItemSelectedListener: ((Int, String) -> Unit)? = null

    private var title: String? = null
    private var options: List<String> = emptyList()
    private var checkedPosition: Int = -1

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
        title = arguments?.getString(ARG_TITLE)
        options = arguments?.getStringArrayList(ARG_OPTIONS) ?: emptyList()
        checkedPosition = arguments?.getInt(ARG_CHECKED_POSITION, -1) ?: -1

        mBinding.apply {
            tvTitle.text = title

            // 动态添加选项
            options.forEachIndexed { index, option ->
                val itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_single_choice_with_check, llOptions, false)

                itemView.findViewById<TextView>(R.id.tv_option).text = option
                // 设置选中状态图标
                itemView.findViewById<ImageView>(R.id.iv_check).visibility =
                    if(index == checkedPosition) View.VISIBLE else View.INVISIBLE

                itemView.setOnClickListener {
                    onItemSelectedListener?.invoke(index, option)
                    dismiss()
                }

                llOptions.addView(itemView)
            }
        }
    }

    companion object {
        private const val ARG_CHECKED_POSITION = "arg_checked_position"

        fun newInstance(
            title: String,
            options: List<String>,
            checkedPosition: Int = -1
        ): SingleChoiceWithCheckDialogFragment {
            return SingleChoiceWithCheckDialogFragment().apply {
                arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_OPTIONS to ArrayList(options),
                    ARG_CHECKED_POSITION to checkedPosition
                )
            }
        }
    }
}
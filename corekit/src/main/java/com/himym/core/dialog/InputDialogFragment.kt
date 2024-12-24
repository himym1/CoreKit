package com.himym.core.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.himym.core.Constants.ARG_CONTENT
import com.himym.core.Constants.ARG_TITLE
import com.himym.core.entity.DialogDisplayConfig
import com.himym.core.ui.BaseDialogFragment
import com.himym.core.utils.screenWidth
import com.himym.corekit.databinding.DialogInputBinding


/**
 * @author himym
 * @description 输入对话框
 */
class InputDialogFragment : BaseDialogFragment<DialogInputBinding>() {

    var onInputConfirmed: ((String) -> Unit)? = null

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
        mBinding.apply {
            val title = arguments?.getString(ARG_TITLE)
            val hint = arguments?.getString(ARG_CONTENT)

            tvUiTitle.text = title
            tvInputMessage.hint = hint

            tvUiConfirm.setOnClickListener {
                val inputText = tvInputMessage.text.toString()
                onInputConfirmed?.invoke(inputText)
                dismiss()
            }
            tvUiCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun dialogFragmentDisplayConfigs() = DialogDisplayConfig(
        dialogWidth = (requireContext().screenWidth * 0.4).toInt(),
        dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT,
        dialogGravity = Gravity.CENTER
    )

    companion object {

        // 用于创建带标题和提示的对话框实例
        fun newInstance(title: String, hint: String = ""): InputDialogFragment {
            return InputDialogFragment().apply {
                this.arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_CONTENT to hint
                )
            }
        }
    }
}
package com.himym.core.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.himym.core.Constants.ARG_CONTENT
import com.himym.core.Constants.ARG_TITLE
import com.himym.core.entity.DialogDisplayConfig
import com.himym.core.extension.setOnDebounceClickListener
import com.himym.core.ui.BaseDialogFragment
import com.himym.core.utils.screenWidth
import com.himym.corekit.databinding.DialogMessageBinding

/**
 * @author himym.
 * @description 等待加载对话框
 */
class MessageDialogFragment : BaseDialogFragment<DialogMessageBinding>() {

    var onConfirmClickListener: (() -> Unit)? = null

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
        mBinding.apply {
            // 获取传入的标题和内容
            val title = arguments?.getString(ARG_TITLE)
            val content = arguments?.getString(ARG_CONTENT)

            tvUiTitle.text = title
            tvMessageMessage.text = content

            // 设置确定和取消按钮的点击事件
            tvUiConfirm.setOnDebounceClickListener {
                onConfirmClickListener?.invoke()
                dismiss()
            }

            tvUiCancel.setOnDebounceClickListener {
                dismiss()
            }
        }
    }

    override fun dialogFragmentDisplayConfigs() = DialogDisplayConfig(
        dialogWidth = (requireContext().screenWidth * 0.4).toInt(),
        dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT,
        dialogGravity = Gravity.CENTER
    )

    override fun onDestroyView() {
        onConfirmClickListener = null  // 清理匿名函数引用
        mBinding.tvUiConfirm.setOnClickListener(null) // 解绑点击事件
        mBinding.tvUiCancel.setOnClickListener(null)  // 解绑点击事件
        super.onDestroyView()
    }

    companion object {

        // 提供一个实例化方法，用于传递标题和内容
        fun newInstance(title: String, content: String): MessageDialogFragment {
            return MessageDialogFragment().apply {
                arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_CONTENT to content
                )
            }
        }
    }
}
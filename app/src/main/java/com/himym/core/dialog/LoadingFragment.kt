package com.himym.core.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.himym.core.entity.DialogDisplayConfig
import com.himym.core.ui.BaseDialogFragment
import com.himym.core.utils.screenWidth
import com.himym.corekit.databinding.DialogWaitBinding

/**
 * @author himym.
 * @description 等待加载对话框
 */
class LoadingFragment : BaseDialogFragment<DialogWaitBinding>() {

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    // 如果需要自定义窗口配置，可以重写dialogFragmentDisplayConfigs
    override fun dialogFragmentDisplayConfigs() = DialogDisplayConfig(
        dialogWidth = (requireContext().screenWidth * 0.3).toInt(),
        dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT,
        dialogGravity = Gravity.CENTER
    )
}
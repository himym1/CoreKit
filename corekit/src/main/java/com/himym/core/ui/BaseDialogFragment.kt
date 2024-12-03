package com.himym.core.ui

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.himym.core.anno.DialogConfig
import com.himym.core.anno.DialogSizeType
import com.himym.core.entity.DialogDisplayConfig
import com.himym.core.helper.KLogger
import com.himym.core.helper.ePrint
import com.himym.core.listener.OnDialogFragmentCancelListener
import com.himym.core.listener.OnDialogFragmentDismissListener
import com.himym.core.utils.screenHeight
import com.himym.core.utils.screenWidth
import com.himym.corekit.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

/**
 * @author
 * @description BaseDialogFragment
 */
abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment(), CoroutineScope by MainScope(), KLogger {

    var onDialogFragmentDismissListener: OnDialogFragmentDismissListener? = null
    var onDialogFragmentCancelListener: OnDialogFragmentCancelListener? = null

    private var _binding: VB? = null
    protected val mBinding get() = _binding!!

    private var mSavedState = false
    private val mDialogConfig by lazy<DialogConfig?> { javaClass.getAnnotation(DialogConfig::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_Alert)

        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setWindowAnimations(dialogFragmentAnim())
        }

        // 使用反射实例化 ViewBinding
        _binding = inflateViewBinding(inflater, container)
        return mBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val clazz = type as Class<VB>
        val inflateMethod = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )
        return inflateMethod.invoke(null, inflater, container, false) as VB
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mSavedState = true
        super.onSaveInstanceState(outState)
    }

    /**
     * 建议使用此方法来显示对话框，而不是直接调用 [show]
     */
    open fun showAllowStateLoss(manager: FragmentManager, tag: String) {
        if (manager.isStateSaved || mSavedState) return
        show(manager, tag)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val dialogDisplayConfig = if (mDialogConfig != null) {
                val width =
                    if (mDialogConfig!!.widthType == DialogSizeType.FILL_SIZE) {
                        WindowManager.LayoutParams.MATCH_PARENT
                    } else {
                        val fraction = mDialogConfig!!.widthFraction
                        if (fraction == 0f) WindowManager.LayoutParams.WRAP_CONTENT
                        else (requireContext().screenWidth * fraction).toInt()
                    }

                val height =
                    if (mDialogConfig!!.heightType == DialogSizeType.FILL_SIZE) {
                        WindowManager.LayoutParams.MATCH_PARENT
                    } else {
                        val fraction = mDialogConfig!!.heightFraction
                        if (fraction == 0f) WindowManager.LayoutParams.WRAP_CONTENT
                        else (requireContext().screenHeight * fraction).toInt()
                    }

                val backgroundColor = mDialogConfig!!.backgroundColor.run {
                    if (matches(Regex("#([0-9A-Fa-f]{8}|[0-9A-Fa-f]{6})"))) {
                        this
                    } else "#00000000"
                }

                DialogDisplayConfig(
                    width,
                    height,
                    mDialogConfig!!.gravity,
                    ColorDrawable(Color.parseColor(backgroundColor))
                )
            } else {
                dialogFragmentDisplayConfigs()
            }

            setBackgroundDrawable(dialogDisplayConfig.dialogBackground)
            attributes = dialog?.window?.attributes?.apply {
                width = dialogDisplayConfig.dialogWidth
                height = dialogDisplayConfig.dialogHeight
                gravity = dialogDisplayConfig.dialogGravity
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenFlowStates()
        listenFlowEvents()
        initDialog(view, savedInstanceState)
        bindToDBV()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cancel()
        ePrint { "BaseDialogFragment canceled" }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogFragmentDismissListener?.onDialogFragmentDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDialogFragmentCancelListener?.onDialogFragmentCancel(dialog)
    }

    // 定义对话框的进入和退出动画
    open fun dialogFragmentAnim() = R.style.DialogPushInOutAnimation

    open fun dialogFragmentDisplayConfigs() = DialogDisplayConfig(
        (requireContext().screenWidth * 0.75).toInt()
    )

    abstract fun initDialog(view: View, savedInstanceState: Bundle?)

    open fun bindToDBV() {}

    open fun listenFlowStates() {}

    open fun listenFlowEvents() {}
}
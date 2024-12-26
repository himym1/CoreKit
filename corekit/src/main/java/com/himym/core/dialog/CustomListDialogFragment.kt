package com.himym.core.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import com.himym.core.Constants.ARG_TITLE
import com.himym.core.ui.BaseDialogFragment
import com.himym.corekit.databinding.DialogCustomListBinding

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/26
 * @desc: CustomListDialogFragment
 */
class CustomListDialogFragment<T> : BaseDialogFragment<DialogCustomListBinding>() {

    private var title: String? = null
    private var items: List<T> = emptyList()
    private var bindView: ((View, T, Int) -> Unit)? = null
    private var itemLayoutRes: Int = 0
    var onItemSelectedListener: ((Int, T) -> Unit)? = null

    override fun initDialog(view: View, savedInstanceState: Bundle?) {
        title = arguments?.getString(ARG_TITLE)

        @Suppress("UNCHECKED_CAST")
        items = (arguments?.getSerializable(ARG_ITEMS) as? ArrayList<T>) ?: emptyList()

        mBinding.apply {
            tvTitle.text = title

            // 动态添加选项
            items.forEachIndexed { index, item ->
                val itemView = LayoutInflater.from(context)
                    .inflate(itemLayoutRes, llItems, false)

                // 调用自定义的绑定逻辑
                bindView?.invoke(itemView, item, index)

                // 点击事件
                itemView.setOnClickListener {
                    onItemSelectedListener?.invoke(index, item)
                    dismiss()
                }

                llItems.addView(itemView)
            }
        }
    }

    companion object {
        private const val ARG_ITEMS = "arg_items"

        fun <T> newInstance(
            title: String,
            items: List<T>,
            itemLayoutRes: Int,
            bindView: (View, T, Int) -> Unit
        ): CustomListDialogFragment<T> {
            return CustomListDialogFragment<T>().apply {
                arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_ITEMS to ArrayList(items)
                )
                this.itemLayoutRes = itemLayoutRes
                this.bindView = bindView
            }
        }
    }
}
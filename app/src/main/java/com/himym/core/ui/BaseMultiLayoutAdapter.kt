package com.himym.core.ui

import android.util.SparseIntArray
import com.himym.core.listener.MultiLayoutImp

/**
 * @author himym.
 * @description
 */
abstract class BaseMultiLayoutAdapter(
    dataList: MutableList<MultiLayoutImp>? = null
) : BaseRecyclerViewAdapter<MultiLayoutImp>(dataList) {

    private val mLayouts = SparseIntArray()

    fun registerAdapterItems(viewType: Int, layoutId: Int) {
        mLayouts.put(viewType, layoutId)
    }

    override fun layoutId(viewType: Int) = mLayouts[viewType]

    override fun getAdapterItemViewType(position: Int) = mDataList[position].viewType()
}
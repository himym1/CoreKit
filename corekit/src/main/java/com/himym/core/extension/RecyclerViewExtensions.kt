package com.himym.core.extension

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.min

/**
 * @author himym.
 * @description RecyclerView 扩展方法
 */

/**
 * 滚动 RecyclerView 到顶部。
 *
 * @param threshold 当滚动的第一个完全可见项大于此值时，才会平滑滚动到顶部。默认值为 10。
 * @param staggeredSpanCount 当使用 StaggeredGridLayoutManager 时的跨度数量，默认值为 2。
 */
fun RecyclerView.scrollToTop(threshold: Int = 10, staggeredSpanCount: Int = 2) {
    // 检查 LayoutManager 是否为空
    val manager = layoutManager ?: return

    when (manager) {
        is GridLayoutManager -> {
            val firstVisibleItem = manager.findFirstCompletelyVisibleItemPosition()
            if (firstVisibleItem == 0) return

            manager.scrollToPositionWithOffset(min(firstVisibleItem, threshold), 0)
            if (firstVisibleItem > threshold) {
                manager.smoothScrollToPosition(this, RecyclerView.State(), 0)
            }
        }

        is LinearLayoutManager -> {
            val firstVisibleItem = manager.findFirstCompletelyVisibleItemPosition()
            if (firstVisibleItem == 0) return

            manager.scrollToPositionWithOffset(min(firstVisibleItem, threshold), 0)
            if (firstVisibleItem > threshold) {
                manager.smoothScrollToPosition(this, RecyclerView.State(), 0)
            }
        }

        is StaggeredGridLayoutManager -> {
            val firstVisibleItems = IntArray(staggeredSpanCount)
            manager.findFirstCompletelyVisibleItemPositions(firstVisibleItems)
            val firstVisibleItem = firstVisibleItems.minOrNull() ?: 0
            if (firstVisibleItem == 0) return

            manager.scrollToPositionWithOffset(min(firstVisibleItem, threshold), 0)
            if (firstVisibleItem > threshold) {
                manager.smoothScrollToPosition(this, RecyclerView.State(), 0)
            }
        }
    }
}

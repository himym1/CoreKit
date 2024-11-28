package com.himym.core.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.himym.core.listener.OnRecyclerItemClickListener
import com.himym.core.listener.OnRecyclerItemLongClickListener
import com.himym.core.ui.BaseRecyclerViewAdapter

/**
 * @author himym.
 * @description BindingAdapter for RecyclerView
 */

/**
 * @param adapter  open debounced is setting by adapter construction
 * @param listener item click or item debounced click
 * @param longListener item long click
 */
@BindingAdapter(value = ["recyclerAdapter", "onRecyclerItemClick", "onRecyclerItemLongClick", "recyclerDivider"], requireAll = false)
fun bindRecyclerAdapter(
    recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?,
    listener: OnRecyclerItemClickListener?,
    longListener: OnRecyclerItemLongClickListener?,
    decor: RecyclerView.ItemDecoration?
) {
    adapter?.let { recyclerView.adapter = it }

    listener?.let { (recyclerView.adapter as? BaseRecyclerViewAdapter<*>)?.onItemClickListener = it }

    longListener?.let { (recyclerView.adapter as? BaseRecyclerViewAdapter<*>)?.onItemLongClickListener = it }

    decor?.let { recyclerView.addItemDecoration(decor) }
}
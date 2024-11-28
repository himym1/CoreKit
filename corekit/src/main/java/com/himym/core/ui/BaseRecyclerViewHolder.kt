package com.himym.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.himym.core.extension.layoutToDataBinding

/**
 * @author himym.
 * @description base recycler view holder
 */
open class BaseRecyclerViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewDataBinding> viewDataBinding(): T? = binding as? T

    companion object {
        fun createHolder(parent: ViewGroup, layout: Int) = BaseRecyclerViewHolder(
            layout.layoutToDataBinding(LayoutInflater.from(parent.context), parent, false)
        )
    }
}
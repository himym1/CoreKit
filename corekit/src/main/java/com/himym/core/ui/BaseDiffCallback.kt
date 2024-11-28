package com.himym.core.ui

import androidx.recyclerview.widget.DiffUtil

/**
 * @author himym.
 * @description base diff callback
 */
abstract class BaseDiffCallback<T : Any>(newItems: MutableList<T>?) : DiffUtil.Callback() {
    private var newList = newItems
    var oldList: MutableList<T>? = null

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oList = oldList ?: return false
        val nList = newList ?: return false
        return areSameItems(oList[oldItemPosition], nList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oList = oldList ?: return false
        val nList = newList ?: return false
        return areSameContent(oList[oldItemPosition], nList[newItemPosition])
    }

    abstract fun areSameItems(old: T, new: T): Boolean

    abstract fun areSameContent(old: T, new: T): Boolean

    fun getNewItems() = newList
}
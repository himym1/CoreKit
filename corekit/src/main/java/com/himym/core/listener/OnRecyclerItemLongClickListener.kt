package com.topping.core.listener

import android.view.View

/**
 * @author himym.
 * @description recycler item long click listener
 */
fun interface OnRecyclerItemLongClickListener {
    fun onRecyclerItemLongClick(position: Int, view: View): Boolean
}
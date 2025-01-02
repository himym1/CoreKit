package com.topping.core.listener

import android.view.View

/**
 * @author himym.
 * @description recycler item click listener
 */
fun interface OnRecyclerItemClickListener {
    fun onRecyclerItemClick(position: Int, view: View)
}
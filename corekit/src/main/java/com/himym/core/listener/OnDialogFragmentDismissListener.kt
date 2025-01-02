package com.topping.core.listener

import android.content.DialogInterface

/**
 * @author himym.
 * @description dialog dismiss listener
 */
fun interface OnDialogFragmentDismissListener {
    fun onDialogFragmentDismiss(dialog: DialogInterface)
}
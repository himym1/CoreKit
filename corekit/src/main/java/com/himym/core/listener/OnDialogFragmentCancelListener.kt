package com.topping.core.listener

import android.content.DialogInterface

/**
 * @author himym.
 * @description dialog cancel listener
 */
fun interface OnDialogFragmentCancelListener {
    fun onDialogFragmentCancel(dialog: DialogInterface)
}
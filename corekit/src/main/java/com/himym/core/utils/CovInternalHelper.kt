package com.himym.core.utils

import android.os.Build

/**
 * @author himym.
 * @description actionsByR
 */
internal fun actionsByR(belowR: () -> Unit, aboveR: () -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) belowR() else aboveR()
}
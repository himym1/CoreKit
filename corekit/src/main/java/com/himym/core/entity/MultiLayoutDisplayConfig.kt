package com.topping.core.entity

import androidx.annotation.IntRange

/**
 * @author himym.
 * @description MultiLayoutDisplayConfig
 * @param displayCount  how many items in a column
 */
data class MultiLayoutDisplayConfig(
    val typeOf: Class<*>,
    @IntRange(from = 1) val displayCount: Int,
    @IntRange(from = 1) val viewType: Int, val layoutId: Int
)
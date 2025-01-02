package com.himym.core.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/29
 * @desc:
 */
@Parcelize
data class User(val id: Int, val name: String = "",val avatar:String = "") : Parcelable

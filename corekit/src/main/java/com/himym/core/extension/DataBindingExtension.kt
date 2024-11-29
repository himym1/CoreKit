package com.himym.core.extension

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author himym.
 * @description DataBinding 扩展函数
 */
fun <T : ViewDataBinding> Int.layoutToDataBinding(
    context: Context, parent: ViewGroup? = null, attached: Boolean = false
): T = DataBindingUtil.inflate(LayoutInflater.from(context), this, parent, attached)

/////////////////////////////////////////
// inflater fragment VB ////////////////
///////////////////////////////////////
fun <T : ViewDataBinding> Int.layoutToDataBinding(
    inflater: LayoutInflater, parent: ViewGroup?, attached: Boolean = false
): T = DataBindingUtil.inflate(inflater, this, parent, attached)

/////////////////////////////////////////
// inflater activity VB ////////////////
///////////////////////////////////////
fun <T : ViewDataBinding> Int.layoutToDataBinding(activity: Activity): T = DataBindingUtil.setContentView(activity, this)
package com.topping.core.base

import androidx.annotation.Keep

@Keep
interface IUIChange

@Keep
interface IUiState : IUIChange

@Keep
interface IUiEvent : IUIChange

@Keep
interface IUiIntent

/**
 *
 * @author: wangjianguo
 * @desc: 加载UI的Intent
 */
sealed class LoadUiIntent {
    data class Loading(var isShow: Boolean) : LoadUiIntent()
    data object ShowMainView : LoadUiIntent()
    data class Error(val msg: String) : LoadUiIntent()
}
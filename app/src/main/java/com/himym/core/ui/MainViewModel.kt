package com.topping.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/27
 * @desc:
 */
class MainViewModel : ViewModel() {

    private val _switchChecked = MutableStateFlow<Boolean?>(null)
    private val _isToolbarReady = MutableStateFlow(false)

    // 组合两个状态
    val toolbarState = combine(
        _switchChecked,
        _isToolbarReady
    ) { switchState, isReady ->
        // 只有当 Toolbar 准备好且有开关状态时才发送
        if (isReady && switchState != null) {
            switchState
        } else {
            null
        }
    }.filterNotNull()

    fun setToolbarReady() {
        _isToolbarReady.value = true
    }

    fun loadSwitchState(isSwitch :Boolean) {
        viewModelScope.launch {
            // 模拟网络请求
            _switchChecked.value = isSwitch
        }
    }
}
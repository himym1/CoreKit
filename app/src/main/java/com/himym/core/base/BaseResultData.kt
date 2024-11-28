package com.himym.core.base


/**
 *  @author himym
 *  create at 2022/10/27 1:06
 *  description: 基础data
 */
data class BaseResultData<T>(
    val data: T, // 业务数据
    val code: Int, // 状态码
    val msg: String, // 信息
    var state: ReqState = ReqState.Error
) {
    fun isSuccess(): Boolean {
        return code == 200
    }
}

enum class ReqState {
    Success, Error
}


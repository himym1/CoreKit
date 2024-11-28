package com.himym.core.base

import kotlinx.coroutines.flow.FlowCollector

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/26
 * @desc: 基础Repository
 */
open class BaseRepository {
    protected suspend fun <T : Any> FlowCollector<BaseResultData<T>>.emitBaseData(baseData: BaseResultData<T>) {
        if (baseData.code == 0) {
            baseData.state = ReqState.Success
        } else {
            baseData.state = ReqState.Error
        }
        return emit(baseData)
    }
}
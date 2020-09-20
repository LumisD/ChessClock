package com.lumisdinos.tabletransform.common.extension

import androidx.lifecycle.MutableLiveData
import com.lumisdinos.chessclock.common.Resource
import com.lumisdinos.chessclock.common.ResourceState

typealias LiveResult<T> = MutableLiveData<Resource<T>>

@JvmName("postSuccessResult")
fun <T> LiveResult<T>.postSuccess(data: T) = postValue(Resource(ResourceState.SUCCESS, data))

@JvmName("postLoadingResult")
fun <T> LiveResult<T>.postLoading() = postValue(Resource(ResourceState.LOADING, value?.data))

@JvmName("postHideLoadingResult")
fun <T> LiveResult<T>.postHideLoading() = postValue(Resource(ResourceState.HIDE_LOADING, value?.data))

@JvmName("postErrorResult")
fun <T> LiveResult<T>.postError(message: String? = null) = postValue(Resource(ResourceState.ERROR, value?.data, message))

@JvmName("postEmptyResult")
fun <T> LiveResult<T>.postEmpty() = postValue(Resource(ResourceState.EMPTY))

@JvmName("postCancelResult")
fun <T> LiveResult<T>.postCancel() = postValue(Resource(ResourceState.CANCEL))

@JvmName("postRefreshResult")
fun <T> LiveResult<T>.postRefresh(data: T?) = postValue(Resource(ResourceState.REFRESH, data ?: value?.data))
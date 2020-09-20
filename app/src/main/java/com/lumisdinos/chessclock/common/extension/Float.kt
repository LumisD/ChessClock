package com.lumisdinos.tabletransform.common.extension

@JvmName("postSuccessResult")
fun Float?.toNotNull(): Float {
    return this ?: 0f
}
package com.lumisdinos.chessclock.common

sealed class ResourceState {
    object LOADING : ResourceState()
    object HIDE_LOADING : ResourceState()
    object SUCCESS : ResourceState()
    object ERROR : ResourceState()
    object CANCEL : ResourceState()
    object EMPTY : ResourceState()
    object REFRESH : ResourceState()
}
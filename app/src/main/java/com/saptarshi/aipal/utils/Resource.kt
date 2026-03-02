package com.saptarshi.aipal.utils

sealed class Resource<out T>(val data: T? = null, val message: String? = null) {
    object Loading : Resource<Nothing>()
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error(message: String) : Resource<Nothing>(null, message)
}
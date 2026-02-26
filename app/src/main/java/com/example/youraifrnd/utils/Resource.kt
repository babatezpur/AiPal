package com.example.youraifrnd.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    object Loading : Resource<Nothing>()
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String, data : T? = null) : Resource<Nothing>(null, message)
}
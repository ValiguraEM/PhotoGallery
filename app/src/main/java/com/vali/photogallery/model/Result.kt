package com.vali.photogallery.model

import java.lang.Exception

sealed class Result<T>(var data: T? = null, val message: String? = null, val exception: Exception? = null) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String, data: T? = null, exception: Exception) : Result<T>(data, message, exception)
}
package com.natasha.clockio.base.model

//https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.kt
data class BaseResponse<out T>(val status: Status, val success: Boolean, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?) : BaseResponse<T> {
            return BaseResponse(Status.SUCCESS, true, data, null)
        }

        fun <T> failed(data: T?) : BaseResponse<T> { // in retrofit onResponse but notSuccessful
            return BaseResponse(Status.SUCCESS, false, data, null)
        }

        fun<T> error(msg: String?, data: T?) : BaseResponse<T> {
            return BaseResponse(Status.ERROR, false, data, msg)
        }

        fun<T> loading(data: T?): BaseResponse<T> {
            return BaseResponse(Status.LOADING, true, data, null)
        }
    }
}
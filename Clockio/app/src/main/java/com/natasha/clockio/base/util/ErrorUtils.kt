package com.natasha.clockio.base.util

import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

//https://www.woolha.com/tutorials/android-retrofit-2-custom-error-response-handling
class ErrorUtils {
    fun <T> parseError(response: Response<out T>?) : Error {
        var body: JSONObject?
        var success: Boolean
        var message: String
        try {
            val errorBody: String = response!!.errorBody()!!.string()
            body = JSONObject(errorBody)
            success = body.getBoolean("success")
            message = body.getString("message")
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        return com.natasha.clockio.base.model.Error(body.getBoolean("success"))
        return Error("error sesuatu")
    }
}
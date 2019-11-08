package com.natasha.clockio.login.data

import com.natasha.clockio.login.service.AuthApi
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val authApi: AuthApi) {


    //    fun login(username: String, password: String): BaseResponse<AccessToken> {
//        try {
//            val response = authApi.requestToken(username, password)
//            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
//            return Result.Success(fakeUser)
//            if (response.isSuccessful) {
//                return Result.Success(response.body()!!)
//            return BaseResponse.success(response.body()!!)
//            }
//            else {
//                val error = response.errorBody()
//                return Result.Throw(response.errorBody())
//            }
//        } catch (e: Throwable) {
//            return Result.Error(IOException("Error logging in", e))
//            return BaseResponse.error(e.message!!, null)
//        }
//    }
//    fun login(username: String, password: String): Call<AccessToken> = authApi.requestToken(username, password, "password")

    suspend fun login(username: String, password: String) = authApi.requestToken(username, password, "password")

fun logout() {
    // TODO: revoke authentication
}
}


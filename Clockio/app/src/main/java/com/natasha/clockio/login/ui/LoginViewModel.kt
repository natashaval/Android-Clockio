package com.natasha.clockio.login.ui

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.natasha.clockio.R
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.login.data.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    //    private val _loginResult = MutableLiveData<LoginResult>()
    private val _loginResult = MutableLiveData<BaseResponse<AccessToken>>()
    val loginResult: LiveData<BaseResponse<AccessToken>> = _loginResult

    private val _loginFailed = MutableLiveData<ResponseBody>()
    val loginFailed: LiveData<ResponseBody>
        get() = _loginFailed

    private val _profile = MutableLiveData<BaseResponse<Any>>()
    val profile: LiveData<BaseResponse<Any>>
        get() = _profile

    //    https://medium.com/@cesarmcferreira/how-to-use-the-new-android-viewmodelscope-in-clean-architecture-2a33aac959ee
//    https://proandroiddev.com/coroutines-with-architecture-components-4c223a51b112
    fun login(username: String, password: String) {
        loginRepository.login(username, password).enqueue(object: Callback<AccessToken> {
            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
            }

            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    _loginResult.value = BaseResponse.success(response.body())
                } else {
                    _loginFailed.value = response.errorBody()
                }
            }

        })
    }

    fun loadProfile() =
        viewModelScope.launch {
            Log.d(TAG, "getProfile is called in view model")
            _profile.value = loginRepository.getProfile()
        }
        /*liveData(Dispatchers.IO) {
            emit(BaseResponse.loading(null))
            val result = loginRepository.getProfile()
            _profile.value = result
            emit(result)
        }*/


    //https://medium.com/@harmittaa/retrofit-2-6-0-with-koin-and-coroutines-network-error-handling-a5b98b5e5ca0

    /*fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        emit(BaseResponse.loading(null))
        Log.d(TAG, "login is called from short view model livedata")
        try {
            val result = loginRepository.login(username, password)
            _loginResult.value = result
            emit(result)
        } catch (e: Exception) {
            Log.e(TAG, "login is NOT called from livedata")
        }
    }*/

/* punya Alfian terus disini LoginActivity loginViewModel.viewModelScope.launch { this function }
    suspend fun login(username: String, password: String): BaseResponse<AccessToken> {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)
        Log.d(TAG, "login is called in live data emit")
        return result
        Log.d(TAG, "login is finished in view model")
    }

 */

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value =
                LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value =
                LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }
}

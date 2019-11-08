package com.natasha.clockio.login.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.liveData
import com.natasha.clockio.R
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.Response
import com.natasha.clockio.login.data.LoginRepository
import com.natasha.clockio.login.data.Result
import com.natasha.clockio.login.repository.LoginaRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import java.lang.Exception
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    //    private val _loginResult = MutableLiveData<LoginResult>()
    private val _loginResult = MutableLiveData<Response<AccessToken>>()
    val loginResult: LiveData<Response<AccessToken>> = _loginResult

    private val _loginFailed = MutableLiveData<ResponseBody>()
    val loginFailed: LiveData<ResponseBody>
    get() = _loginFailed

    /*
    fun login(username: String, password: String) {
        Log.d(TAG, "login is called in view model")
        loginRepository.login(username, password,
            { accessToken -> _loginResult.value = accessToken},
            { err -> _loginFailed.value = err },
            {t -> Log.e(TAG, "onFailure: ", t)})

        Log.d(TAG, "Login view model has changed " + loginResult.value.toString())
    }
     */

    //https://medium.com/@harmittaa/retrofit-2-6-0-with-koin-and-coroutines-network-error-handling-a5b98b5e5ca0

    suspend fun login(username: String, password: String): Response<AccessToken> {
        Log.d(TAG, "login is called in view model")
//        liveData {
//            emit (Response.loading(null))
//            try {
                val result = loginRepository.login(username, password)
                Log.d(TAG, "login is called in live data emit")
//                _loginResult.value = result
//            _loginResult.postValue(result)
        return result
//                emit(result)
//            } catch (e: Exception) {
//                Log.e(TAG, "login is NOT called in view model repository")
//            }
//        }
        Log.d(TAG, "login is finished in view model")
    }

    /*fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        emit(Response.loading(null))
        Log.d(TAG, "login is called from short view model livedata")
        try {
            val result = loginRepository.login(username, password)
            _loginResult.value = result
            emit(result)
        } catch (e: Exception) {
            Log.e(TAG, "login is NOT called from livedata")
        }
    }*/

/*
    suspend fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(
                    success = LoggedInUserView(
                        displayName = result.data.displayName
                    )
                )
        } else {
            _loginResult.value =
                LoginResult(error = R.string.login_failed)
        }
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

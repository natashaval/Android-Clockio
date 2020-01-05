package com.natasha.clockio.login.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.natasha.clockio.R
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.constant.UrlConst
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.LoggedInUser
import com.natasha.clockio.base.util.RetrofitInterceptor
import com.natasha.clockio.base.util.observeOnce
import com.natasha.clockio.home.ui.HomeActivity
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okhttp3.internal.lockAndWaitNanos
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {
  private val TAG: String = LoginActivity::class.java.simpleName

  @Inject lateinit var factory: ViewModelProvider.Factory
  @Inject lateinit var interceptor: RetrofitInterceptor
  @Inject lateinit var sharedPref: SharedPreferences
  private lateinit var loginViewModel: LoginViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_login)
    AndroidInjection.inject(this)

    interceptor.setBasic(UrlConst.CLIENT_ID, UrlConst.CLIENT_SECRET)
    clearSharedPref()
    loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

    loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
      val loginState = it ?: return@Observer

      // disable login button unless both username / password is valid
      login.isEnabled = loginState.isDataValid

      if (loginState.usernameError != null) {
        username.error = getString(loginState.usernameError)
      }
      if (loginState.passwordError != null) {
        password.error = getString(loginState.passwordError)
      }
    })


    loginViewModel.loginResult.observeOnce(this@LoginActivity, Observer {
      val loginResult = it ?: return@Observer
      var isLogin = false
      loading.visibility = View.GONE

      Log.d(TAG, "login is called from login result $loginResult")
      Log.d(TAG, "msg: ${loginResult.message} data: ${loginResult.data?.accessToken}")
      loginResult.data?.let { token ->
        interceptor.setToken(token.accessToken)
        val editor = sharedPref.edit()
        editor.putString(PreferenceConst.ACCESS_TOKEN_KEY, token.accessToken)
        editor.putString(PreferenceConst.REFRESH_TOKEN_KEY, token.refreshToken)
        editor.commit()
        if (!TextUtils.isEmpty(token.accessToken)) isLogin = true
      }
      setResult(Activity.RESULT_OK)

      if (isLogin) {
        var tkn = sharedPref.getString(PreferenceConst.ACCESS_TOKEN_KEY, "")
        Log.d(TAG, "isLogin getProfile triggered $tkn")
        loginViewModel.loadProfile()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
      }

      //Complete and destroy login activity once successful
      finish()
    })

    loginViewModel.loginFailed.observe(this@LoginActivity, Observer {
      loading.visibility = View.GONE
      Log.e(TAG, "Login Failed: $it")
      showLoginFailed(R.string.login_failed)
    })

    loginViewModel.profile.observe(this, Observer { result ->
      when(result.status) {
        BaseResponse.Status.LOADING -> loading.visibility = View.VISIBLE
        BaseResponse.Status.SUCCESS -> {
          loading.visibility = View.GONE
          if (result.success) {
            Log.d(TAG, "profile Success ${result.data}")
            val loggedInUser: LoggedInUser = result.data as LoggedInUser
            val editor = sharedPref.edit()
            editor.putString(PreferenceConst.EMPLOYEE_ID_KEY, loggedInUser.employeeId)
            Log.d(TAG, "employee-id ${loggedInUser.employeeId}")
            editor.apply()
          }
          else {
            val errorBody = result.data as ResponseBody
            Toast.makeText(this, errorBody.string(), Toast.LENGTH_SHORT).show()
          }
        }
        BaseResponse.Status.ERROR -> {
          Log.d(TAG, "profile Error ${result.message}")
          Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
        }
      }
    })

    username.afterTextChanged {
      loginViewModel.loginDataChanged(
          username.text.toString(),
          password.text.toString()
      )
    }

    password.apply {
      afterTextChanged {
        loginViewModel.loginDataChanged(
            username.text.toString(),
            password.text.toString()
        )
      }
    }

    login.setOnClickListener {
      loading.visibility = View.VISIBLE
      Log.d(TAG, "login is called from activity")
      clearSharedPref()
      loginViewModel.login(username.text.toString(), password.text.toString())
    }
  }

  private fun updateUiWithUser(model: LoggedInUserView) {
    val welcome = getString(R.string.welcome)
    val displayName = model.displayName
    Toast.makeText(
        applicationContext,
        "$welcome $displayName",
        Toast.LENGTH_LONG
    ).show()
  }

  private fun showLoginFailed(@StringRes errorString: Int) {
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
  }

  private fun showLoading() {
    loading.visibility = View.VISIBLE
    Log.d(TAG, "login return response loading")
  }
  private fun showError(message: String?) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    Log.d(TAG, "login return response error")
  }

  private fun clearSharedPref() {
    sharedPref.edit().clear().commit()
  }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
  this.addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(editable: Editable?) {
      afterTextChanged.invoke(editable.toString())
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
  })
}

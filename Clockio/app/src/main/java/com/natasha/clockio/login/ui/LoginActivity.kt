package com.natasha.clockio.login.ui

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.natasha.clockio.R
import com.natasha.clockio.base.model.Response
import com.natasha.clockio.base.util.RetrofitInterceptor
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {
    private val TAG: String = LoginActivity::class.java.simpleName

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var interceptor: RetrofitInterceptor
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        AndroidInjection.inject(this)

        interceptor.setBasic(getString(R.string.client_id), getString(R.string.client_secret))
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


        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            /*if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }*/
            Log.d(TAG, "login is called from login result $loginResult")
            when(loginResult.status) {
                Response.Status.SUCCESS -> {
                    Log.d(TAG, "msg: ${loginResult.message} data: ${loginResult.data}")
                }
                Response.Status.LOADING -> showLoading()
                Response.Status.ERROR -> {
                    showError(loginResult.message!!)
                }
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        loginViewModel.loginFailed.observe(this@LoginActivity, Observer {
            loading.visibility = View.GONE
            Toast.makeText(this, it.string(), Toast.LENGTH_SHORT).show()
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

            /*setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }*/

        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            Log.d(TAG, "login is called from activity")
            loginViewModel.viewModelScope.launch {
                // harus diginikan supaya coroutinenya jalan, kalau yang cuma panggil fungsi tidak ada yang observe fungsinya
                val hasil = loginViewModel.login(username.text.toString(), password.text.toString())
                Log.d(TAG, "data: {$hasil.data} msg: ${hasil.message}")
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
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

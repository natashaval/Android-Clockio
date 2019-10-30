package com.natasha.clockio.login.ui.login

import android.app.Activity
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.natasha.clockio.R
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.service.AuthService
import com.natasha.clockio.base.util.RetrofitInterceptor
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {
    val TAG: String? = LoginActivity::class.simpleName

    private lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var interceptor: RetrofitInterceptor

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        AndroidInjection.inject(this)
        basicAuthClick()

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

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
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
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

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
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

    private fun basicAuthClick() {
        basicAuthButton.setOnClickListener {
            interceptor.setBasic("client", "SuperSecret")
            val authApi = retrofit!!.create(AuthService::class.java)
            authApi.requestToken(username.text.toString(), password.text.toString(), "password")
                .enqueue(object: Callback<AccessToken> {
                    override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                        Log.e(TAG, t.message)
                        Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                        if (response.code() == 200) {
                            val token: AccessToken = response.body()!!
                            Log.d(TAG, "Access Token acquired: " + token.accessToken)
//                            Toast.makeText(this@LoginActivity, token.accessToken, Toast.LENGTH_SHORT).show()
                            val editor: SharedPreferences.Editor = sharedPref.edit()
                            editor.putString("access_token", token.accessToken)
                            editor.putString("refresh_token", token.refreshToken)
                            editor.apply()
                        }
                    }

                })
        }
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

package com.natasha.clockio

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.natasha.clockio.base.di.qualifier.ApplicationContext
import com.natasha.clockio.base.model.Test
import com.natasha.clockio.base.model.User
import com.natasha.clockio.base.service.TestService
import com.natasha.clockio.base.util.RetrofitInterceptor
import com.natasha.clockio.login.ui.login.LoginActivity
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    val TAG: String? = MainActivity::class.simpleName

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var interceptor: RetrofitInterceptor

    @Inject
    lateinit var sharedPref: SharedPreferences

    companion object {
        lateinit var testApi: TestService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInButton.setOnClickListener {
            Toast.makeText(this@MainActivity, sharedPref.getString("test", "Ga kesimpen"), Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        AndroidInjection.inject(this)

        getTest()
        getApix()
    }

    private fun getTest() {
        testButton.setOnClickListener{
            testApi = retrofit!!.create(TestService::class.java)
            testApi.getTest().enqueue(object : Callback<Test> {
                override fun onFailure(call: Call<Test>, t: Throwable) {
                    Log.e(TAG, t.message)
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Test>, response: Response<Test>) {
                    if (response.code() == 200) {
                        Log.d(TAG, response.body()!!.toString())
                        val test: String = response.body()!!.test
                        helloTextView.text = test
                        val editor = sharedPref.edit()
                        editor.putString("test", test)
                        editor.apply()
                    }
                }
            })
        }
    }

    private fun getApix() {
        aboutButton.setOnClickListener {
            val token = sharedPref.getString("access_token", "1234")
            interceptor.setToken(token)
            testApi.getProfile().enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, t.message)
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == 200) {
                        Log.d(TAG, response.body()!!.toString())
                        val user: User = response.body()!!
                        helloTextView.text = user.username
                    }
                }
            })
        }
    }
}

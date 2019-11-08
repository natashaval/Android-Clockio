package com.natasha.clockio

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.natasha.clockio.base.model.Test
import com.natasha.clockio.base.util.RetrofitInterceptor
import com.natasha.clockio.login.ui.LoginActivity
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import javax.inject.Inject
import androidx.lifecycle.Observer


class MainActivity : DaggerAppCompatActivity() {
    val TAG: String? = MainActivity::class.simpleName

    @Inject lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var interceptor: RetrofitInterceptor

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInButton.setOnClickListener {
            Toast.makeText(this@MainActivity, sharedPref.getString("test", "Ga kesimpen"), Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        AndroidInjection.inject(this)
        // https://stackoverflow.com/questions/53903762/viewmodelproviders-is-deprecated-in-1-1-0
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        getTestAuto()
        getTest()
        getApix()
    }

    private fun getTest() {
        testButton.setOnClickListener{
            /*testApi = retrofit!!.create(TestApi::class.java)
            testApi.getTest().enqueue(object : Callback<Test> {
                override fun onFailure(call: Call<Test>, t: Throwable) {
                    Log.e(TAG, t.message)
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Test>, response: BaseResponse<Test>) {
                    if (response.code() == 200) {
                        Log.d(TAG, response.body()!!.toString())
                        val test: String = response.body()!!.test
                        helloTextView.text = test
                        val editor = sharedPref.edit()
                        editor.putString("test", test)
                        editor.apply()
                    }
                }
            })*/

            viewModel.testLiveData.observe(this, Observer {
                val test: Test = it.body()!!
                helloTextView.text = test.test
                val editor = sharedPref.edit()
                editor.putString("test", test.test)
                editor.apply()
            })
        }
    }

    private fun getTestAuto() {
        viewModel.testData.observe(this, Observer {
            helloTextView.text = it!!.test
        })
    }

    private fun getApix() {
        aboutButton.setOnClickListener {
            val token = sharedPref.getString("access_token", "1234")
            interceptor.setToken(token!!)
            /*testApi.getProfile().enqueue(object : Callback<LoggedInUser> {
                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                    Log.e(TAG, t.message)
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<LoggedInUser>, response: BaseResponse<LoggedInUser>) {
                    if (response.code() == 200) {
                        Log.d(TAG, response.body()!!.toString())
                        val user: LoggedInUser = response.body()!!
                        helloTextView.text = user.username
                    }
                }
            })*/
        }
    }
}

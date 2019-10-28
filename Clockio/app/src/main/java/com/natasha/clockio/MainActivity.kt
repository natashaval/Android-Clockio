package com.natasha.clockio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.natasha.clockio.base.di.application.MyApplication
import com.natasha.clockio.base.di.component.ActivityComponent
import com.natasha.clockio.base.di.component.DaggerActivityComponent
import com.natasha.clockio.base.di.component.DaggerMainActivityComponent
import com.natasha.clockio.base.di.component.MainActivityComponent
import com.natasha.clockio.base.di.module.MainActivityContextModule
import com.natasha.clockio.base.di.qualifier.ApplicationContext
import com.natasha.clockio.base.model.Test
import com.natasha.clockio.base.service.TestService
import com.natasha.clockio.login.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    val TAG: String? = MainActivity::class.simpleName

        lateinit var mainActivityComponent : MainActivityComponent
//    lateinit var activityComponent: ActivityComponent

    @Inject @ApplicationContext
    lateinit var mContext: Context

//    @Inject @ActivityContext
//    lateinit var activityContext: Context

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        val applicationComponent = MyApplication.get(this).applicationComponent
        Log.d(TAG, "Application component in main activity");
         mainActivityComponent = DaggerMainActivityComponent.builder()
            .mainActivityContextModule(MainActivityContextModule(this))
            .applicationComponent(applicationComponent)
            .build()

        mainActivityComponent.injectMainActivity(this)

//        activityComponent = DaggerActivityComponent.builder()
//                .applicationComponent(applicationComponent)
//                .build()
//
//        activityComponent.inject(this)

        getTest()
    }

    private fun getTest() {
        testButton.setOnClickListener{
            val testApi = retrofit!!.create(TestService::class.java)
            testApi.getTest().enqueue(object : Callback<Test> {
                override fun onFailure(call: Call<Test>, t: Throwable) {
                    Log.e(TAG, t.message)
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Test>, response: Response<Test>) {
                    if (response.code() == 200) {
                        Log.d(TAG, response.body()!!.toString())
                        helloTextView.text =response.body()!!.test
                    }
                }
            })
        }
    }
}

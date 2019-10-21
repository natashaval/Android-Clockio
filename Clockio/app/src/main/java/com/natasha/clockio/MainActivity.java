package com.natasha.clockio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.natasha.clockio.model.Test;
import com.natasha.clockio.service.LoginService;
import com.natasha.clockio.ui.login.LoginActivity;
import com.natasha.clockio.utils.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnTest, btnLogin;
    private TextView tvAbout;
    LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        clickButtonLogin();
        loginService = RetrofitInstance.getClient().create(LoginService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void findViews() {
        btnTest = (Button) findViewById(R.id.btn_test);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvAbout = (TextView) findViewById(R.id.tv_about);
    }

    private void clickButtonLogin() {
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Test> testCall = loginService.getTestJson();
                testCall.enqueue(new Callback<Test>() {
                    @Override
                    public void onResponse(Call<Test> call, Response<Test> response) {
                        String test = response.body().getTest();
                        Log.d("REsponse BOdy", test.toString());
                        tvAbout.setText(test);
                    }

                    @Override
                    public void onFailure(Call<Test> call, Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
            }
        });
    }
}

package com.natasha.clockio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.natasha.clockio.data.model.AccessToken;
import com.natasha.clockio.databinding.ActivityLogin2Binding;
import com.natasha.clockio.ui.AuthListener;
import com.natasha.clockio.ui.AuthViewModel;
import com.natasha.clockio.ui.home.DashboardActivity;

public class Login2Activity extends AppCompatActivity implements AuthListener {

    public ProgressBar progressBar;
    public SharedPreferences preferences;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login2);
        ActivityLogin2Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_login2);
        AuthViewModel viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        binding.setViewModel(viewModel);

        viewModel.authListener = this;
        context = getApplicationContext();
        findViews();
        preferences = getSharedPreferences("Token", Context.MODE_PRIVATE);
    }

    public void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);
    }

    @Override
    public void onStarted() {
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Login Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(LiveData<AccessToken> loginResponse) {
//        Toast.makeText(this, "Login Success" + loginResponse, Toast.LENGTH_SHORT).show();
        final Context context = this;
        loginResponse.observe(this, new Observer<AccessToken>() {
            @Override
            public void onChanged(AccessToken token) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Login Success" + token.getAccessToken(), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("access_token", token.getAccessToken());
                editor.putString("refresh_token", token.getRefreshToken());
                editor.apply();

                Intent intent = new Intent(context, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onFailure(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

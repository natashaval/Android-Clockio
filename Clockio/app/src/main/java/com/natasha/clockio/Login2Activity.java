package com.natasha.clockio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.natasha.clockio.databinding.ActivityLogin2Binding;
import com.natasha.clockio.ui.AuthListener;
import com.natasha.clockio.ui.AuthViewModel;

public class Login2Activity extends AppCompatActivity implements AuthListener {

    public ProgressBar progressBar;
    public EditText usernameEdit, passwordEdit;
    public Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login2);
        ActivityLogin2Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_login2);
        AuthViewModel viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        binding.setViewModel(viewModel);

        viewModel.authListener = this;
        findViews();
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
    public void onSuccess(LiveData<String> loginResponse) {
//        Toast.makeText(this, "Login Success" + loginResponse, Toast.LENGTH_SHORT).show();
        final Context context = this;
        loginResponse.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Login Success" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFailure(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

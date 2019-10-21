package com.natasha.clockio.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.natasha.clockio.data.repository.UserRepository;

//https://www.youtube.com/watch?v=1GBCg70G7cI
//https://github.com/probelalkhan/android-mvvm-architecture/commit/1fba9f9adb184600d381179413b7f060fc5e5cd8
public class AuthViewModel extends ViewModel {

    public static final String TAG = AuthViewModel.class.getSimpleName();

    public String username;
    public String password;
    public AuthListener authListener;
//    public Context context;

    public void onLoginButtonClick(View view) {
        Log.d(TAG, "Button Login clicked" + username + password);
        authListener.onStarted();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            authListener.onFailure("Invalid username or password");
            return;
        }

        LiveData<String> loginResponse = new UserRepository().login(username, password);
        authListener.onSuccess(loginResponse);
    }
}

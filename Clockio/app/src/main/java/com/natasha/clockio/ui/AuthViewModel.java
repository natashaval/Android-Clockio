package com.natasha.clockio.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.natasha.clockio.data.model.AccessToken;
import com.natasha.clockio.data.repository.UserRepository;

//https://www.youtube.com/watch?v=1GBCg70G7cI
//https://github.com/probelalkhan/android-mvvm-architecture/commit/1fba9f9adb184600d381179413b7f060fc5e5cd8
public class AuthViewModel extends AndroidViewModel {

    public static final String TAG = AuthViewModel.class.getSimpleName();

    public String username;
    public String password;
    public AuthListener authListener;
    public String token;

    public AuthViewModel(@NonNull Application application) {
        super(application);
    }

    public void onLoginButtonClick(View view) {
        Log.d(TAG, "Button Login clicked" + username + password);
        authListener.onStarted();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            authListener.onFailure("Invalid username or password");
            return;
        }

        LiveData<AccessToken> loginResponse = new UserRepository().login(username, password);
        authListener.onSuccess(loginResponse);
    }

    public void onToken(View view) {
//        if (isChecked) {
            SharedPreferences preferences = getApplication().getSharedPreferences("Token", Context.MODE_PRIVATE);
            String accessToken = preferences.getString("access_token", "access token ga ada");
            String refreshToken = preferences.getString("refresh_token", "refresh ga ada");
            token = accessToken + " @ " + refreshToken;
            Log.d(TAG, token);
//            Toast.makeText(getApplication(), "Access: " + accessToken + " @ " + refreshToken, Toast.LENGTH_SHORT).show();
//        } else {
            // The toggle is disabled
//        }
    }
}

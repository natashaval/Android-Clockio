package com.natasha.clockio.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.natasha.clockio.data.repository.UserRepository;
import com.natasha.clockio.model.User;

public class ProfileViewModel extends ViewModel {
    private static final String TAG = ProfileViewModel.class.getSimpleName();

    private MutableLiveData<User> userResult = new MutableLiveData<>();
//    private UserRepository userRepository;
//
//    public ProfileViewModel(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public LiveData<User> getUserResult() {
        return userResult;
    }

    public void getProfile(String accessToken){
        Log.d(TAG, "Profile View Model get Profile");
        userResult = new UserRepository().getProfile(accessToken);
    }
}

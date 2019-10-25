package com.natasha.clockio.ui;

import androidx.lifecycle.LiveData;

import com.natasha.clockio.data.model.AccessToken;

public interface AuthListener {
    void onStarted();
    void onSuccess(LiveData<AccessToken> loginResponse);
    void onFailure(String message);
}

package com.natasha.clockio.ui;

import androidx.lifecycle.LiveData;

public interface AuthListener {
    void onStarted();
    void onSuccess(LiveData<String> loginResponse);
    void onFailure(String message);
}

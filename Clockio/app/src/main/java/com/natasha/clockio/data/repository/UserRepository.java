package com.natasha.clockio.data.repository;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.natasha.clockio.R;
import com.natasha.clockio.data.model.AccessToken;
import com.natasha.clockio.service.LoginService;
import com.natasha.clockio.utils.RetrofitGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

//    private Context context;
//
//    public UserRepository(Context context) {
//        this.context = context;
//    }

    public static final String TAG = UserRepository.class.getSimpleName();

//    https://medium.com/@amtechnovation/android-architecture-component-mvvm-part-1-a2e7cff07a76
    public LiveData<AccessToken> login (String username, String password) {
        Log.d(TAG, "User Repository call Retrofit MutableLiveData");

        final MutableLiveData<AccessToken> loginResponse = new MutableLiveData<>();

        LoginService loginService = RetrofitGenerator.createService(LoginService.class,
        "client", "SuperSecret");
//        Resources res = context.getResources();
//        context.getString(R.string.client_id), context.getString(R.string.client_secret));

//        Call<AccessToken> tokenCall = loginService.requestToken(username, password, context.getString(R.string.grant_password));
        Call<AccessToken> tokenCall = loginService.requestToken(username, password, "password");
        tokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    loginResponse.setValue(response.body());
                    Log.d(TAG, "Access Token" + response.body().getAccessToken());
                } else {
                    Log.d(TAG, "Failed Request" + response.errorBody().toString());
                    loginResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(TAG, "Error in retrofit auth: " + t.getMessage());
                loginResponse.setValue(null);
            }
        });

        return loginResponse;
    }
}

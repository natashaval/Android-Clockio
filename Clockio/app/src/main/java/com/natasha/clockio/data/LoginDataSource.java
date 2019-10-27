package com.natasha.clockio.data;

import android.content.res.Resources;
import android.util.Log;

import com.natasha.clockio.R;
import com.natasha.clockio.data.model.AccessToken;
import com.natasha.clockio.data.model.LoggedInUser;
import com.natasha.clockio.service.LoginService;
import com.natasha.clockio.utils.RetrofitGenerator;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static final String TAG = LoginDataSource.class.getSimpleName();
    String myToken;

    public Result<LoggedInUser> login(String username, String password) {
        LoginService loginService = RetrofitGenerator
            .createService(LoginService.class, "client", "SuperSecret");

        Call<AccessToken> accessToken = loginService.requestToken(username, password, "password");
        accessToken.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    myToken = response.body().getAccessToken();
                    Log.d(TAG, "Access Token acquired: " + myToken);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            myToken);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}

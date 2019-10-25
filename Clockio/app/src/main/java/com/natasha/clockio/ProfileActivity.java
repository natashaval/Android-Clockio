package com.natasha.clockio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.natasha.clockio.model.User;
import com.natasha.clockio.ui.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ProfileViewModel profileViewModel;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileViewModel = ViewModelProviders.of(this)
                .get(ProfileViewModel.class);

        final TextView profileTextView = findViewById(R.id.profile);

        SharedPreferences preferences = getSharedPreferences("Token", Context.MODE_PRIVATE);
        token = preferences.getString("access_token", "access ga ada");
        Log.d(TAG, "Profile Activity acquired token: " + token);

        profileViewModel.getProfile(token);

        profileViewModel.getUserResult().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) return;
                Log.d(TAG, "Profile Activity onChanged: " + user.getUsername());
                profileTextView.append(user.getId() + "/n");
                profileTextView.append(user.getUsername() + "/n");
                profileTextView.append("Role: " + user.getRole().getRole() + "/n");
            }
        });
    }
}

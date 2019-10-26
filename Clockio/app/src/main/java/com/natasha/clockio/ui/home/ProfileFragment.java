package com.natasha.clockio.ui.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.natasha.clockio.ProfileActivity;
import com.natasha.clockio.R;
import com.natasha.clockio.model.User;
import com.natasha.clockio.ui.ProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private ProfileViewModel profileViewModel;
    private TextView usernameTextView;
    private String token;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileViewModel = ViewModelProviders.of(this)
                .get(ProfileViewModel.class);

        findViews(view);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
        token = preferences.getString("access_token", "access ga ada");
        Log.d(TAG, "Profile Activity acquired token: " + token);

        getProfile(token);

        return view;
    }

    private void findViews(View view) {
        usernameTextView = view.findViewById(R.id.profile_username);
    }

    private void getProfile(String token) {
        profileViewModel.getProfile(token);

        profileViewModel.getUserResult().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) return;
                Log.d(TAG, "Profile Activity onChanged: " + user.getUsername());
                usernameTextView.append(user.getUsername() + "\n");
            }
        });
    }

}

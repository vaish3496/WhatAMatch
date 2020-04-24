package com.example.tinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileAndSettingsFragment extends Fragment {
    private Button mLogout;
    private FloatingActionButton mEdit;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile_and_settings, container, false);
        mLogout = v.findViewById(R.id.logout);
        mEdit = v.findViewById(R.id.fab_edit);
        mAuth = FirebaseAuth.getInstance();
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutCurrentUser();
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        return v;

    }

    private void logoutCurrentUser() {
        mAuth.signOut();
        Intent intent = new Intent(getActivity(),ChooseLoginRegistrationActivity.class);
        startActivity(intent);
    }

    private void editProfile(){
        Intent intent = new Intent(getActivity(), EditProfile.class);
        startActivity(intent);
    }
}

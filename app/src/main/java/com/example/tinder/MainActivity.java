package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomnav = findViewById(R.id.bottom_nav);
        bottomnav.setOnNavigationItemSelectedListener(navlistner);
        bottomnav.setSelectedItemId(R.id.people);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PeopleFragment()).commit();


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navlistner = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedfragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.personal:
                            selectedfragment = new ProfileAndSettingsFragment();
                            break;
                        case R.id.people:
                            selectedfragment = new PeopleFragment();
                            break;
                        case R.id.matches:
                            selectedfragment = new MatchesFragment();
                    }

                    assert selectedfragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedfragment).commit();
                    return true;

                }
            };

}
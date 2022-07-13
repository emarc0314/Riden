package com.example.riden.activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.riden.R;
import com.example.riden.fragments.AddRideFragment;
import com.example.riden.fragments.MyRidesFragment;
import com.example.riden.fragments.ProfileFragment;
import com.example.riden.fragments.RidesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public BottomNavigationView bottomNavigationView;
    public Toolbar toolbar;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    AddRideFragment addRideFragment = new AddRideFragment();
    MyRidesFragment myRidesFragment = new MyRidesFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    RidesFragment ridesFragment = new RidesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        fragment = profileFragment;
                        break;
                    case R.id.action_availablerides:
                        fragment = ridesFragment;
                        break;
                    case R.id.action_addride:
                        fragment = addRideFragment;
                        break;
                    case R.id.action_myrides:
                        fragment = myRidesFragment;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_availablerides);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            ParseUser.logOut();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
            startActivity(i);
        }
        return true;
    }
}
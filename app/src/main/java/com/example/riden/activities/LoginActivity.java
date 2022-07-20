package com.example.riden.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riden.R;
import com.example.riden.models.User;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    private EditText etPassword;
    private EditText etUsername;
    private Button btNext;
    private Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsernameMaterial);
        etPassword = findViewById(R.id.etPasswordMaterial);
        btNext = findViewById(R.id.btNext);
        btSignUp = findViewById(R.id.btSignUp);

        if(ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signUpUser(username, password);
            }
        });
    }

    private void signUpUser(String username, String password) {
        if(!username.isEmpty() && !password.isEmpty()) {
//            ParseUser user = new ParseUser();
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Toast.makeText(LoginActivity.this, "Success Sign Up!", Toast.LENGTH_SHORT).show();
                        goMainActivity();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Unsuccessful " + e.getStackTrace(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "attempt to login " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT);
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
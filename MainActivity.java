package com.example.safeguard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = findViewById(R.id.btnLogin);
        Button signupButton = findViewById(R.id.btnSignup);


        if (isLoggedIn()) {
            // If user is already logged in, navigate to the next activity
            startActivity(new Intent(MainActivity.this, Main_module_dis.class));
            finish(); // Finish current activity to prevent going back
        } else {
            // If user is not logged in, redirect to the LoginActivity
            startActivity(new Intent(MainActivity.this, Login_Activity.class));
            finish(); // Finish current activity to prevent going back
        }

        // Start the Foreground_Service only when needed


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LoginActivity when the login button is clicked
                Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(intent);
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LoginActivity when the login button is clicked
                Intent intent = new Intent(MainActivity.this, Signup_Activity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isLoggedIn() {
        // Check if user is already logged in based on the stored login status
        return getSharedPreferences("MyPrefs", MODE_PRIVATE).getBoolean("isLoggedIn", false);
    }
}

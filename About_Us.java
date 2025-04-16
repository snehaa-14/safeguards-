package com.example.safeguard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class About_Us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        ImageView logoutButton = findViewById(R.id.buttonLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the main page (LoginActivity in this example)
                Intent intent = new Intent(About_Us.this, Login_Activity.class);
                startActivity(intent);
                // Display a toast message
                Toast.makeText(About_Us.this, "Logout successful. Please login to enter the app.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
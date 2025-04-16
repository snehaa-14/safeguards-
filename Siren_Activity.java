package com.example.safeguard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Siren_Activity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.siren_alert);

        Button btnStartSiren = findViewById(R.id.btnStartSiren);
        Button btnStopSiren = findViewById(R.id.btnStopSiren);
        ImageView logoutButton = findViewById(R.id.buttonLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the main page (LoginActivity in this example)
                Intent intent = new Intent(Siren_Activity.this, Login_Activity.class);
                startActivity(intent);
                // Display a toast message
                Toast.makeText(Siren_Activity.this, "Logout successful. Please login to enter the app.", Toast.LENGTH_SHORT).show();
            }
        });

        btnStartSiren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSiren();
            }
        });

        btnStopSiren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSiren();
            }
        });
    }

    public void startSiren() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.police_siren);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void stopSiren() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

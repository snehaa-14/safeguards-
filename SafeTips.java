package com.example.safeguard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;


public class SafeTips extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safety_tips); // Set the content view to your layout file


        // Set up views using findViewById
        ImageView logo1 = findViewById(R.id.i1);
        TextView tip1 = findViewById(R.id.t1);
        ImageView logoutButton = findViewById(R.id.buttonLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the main page (LoginActivity in this example)
                Intent intent = new Intent(SafeTips.this, Login_Activity.class);
                startActivity(intent);
                // Display a toast message
                Toast.makeText(SafeTips.this, "Logout successful. Please login to enter the app.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set image resource and text
        if (logo1 != null) {
            logo1.setImageResource(R.drawable.fs1);
        }

        if (tip1 != null) {
            tip1.setText("Prepare for the unexpected in everyday life");
        }
    }

    public void playVideo(View view) {
        // Open YouTube video when ImageView is clicked
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=J9lZ9OHdahg"));
        startActivity(intent);
    }
    public void playVideo1(View view) {
        // Open YouTube video when ImageView is clicked
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Duev1Uqvvtc"));
        startActivity(intent);
    }

    public void playVideo2(View view) {
        // Open YouTube video when ImageView is clicked
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/KKN7Ewht1DQ?feature=shared"));
        startActivity(intent);
    }

    public void playVideo3(View view) {
        // Open YouTube video when ImageView is clicked
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/uOiFr8qOu-s?feature=shared"));
        startActivity(intent);
    }
    public void playVideo4(View view) {
        // Open YouTube video when ImageView is clicked
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/cUq5dT1IAAk?feature=shared"));
        startActivity(intent);
    }
    public void playVideo5(View view) {
        // Open YouTube video when ImageView is clicked
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/xm7nx8ujfdo?feature=shared"));
        startActivity(intent);
    }


}
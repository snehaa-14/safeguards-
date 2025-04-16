package com.example.safeguard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class helpline extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_helpline_number);

        TextView textViewHelplineNumber = findViewById(R.id.textViewHelplineNumber);
        TextView textViewHelplineNumber1 = findViewById(R.id.textViewHelplineNumber1);
        TextView textViewHelplineNumber2= findViewById(R.id.textViewHelplineNumber2);
        ImageView logoutButton = findViewById(R.id.buttonLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the main page (LoginActivity in this example)
                Intent intent = new Intent(helpline.this, Login_Activity.class);
                startActivity(intent);
                // Display a toast message
                Toast.makeText(helpline.this, "Logout successful. Please login to enter the app.", Toast.LENGTH_SHORT).show();
            }
        });
        textViewHelplineNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String helplineNumber = ((TextView) v).getText().toString();
                handleHelplineNumberClick(helplineNumber);
            }
        });

        textViewHelplineNumber1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String helplineNumber = ((TextView) v).getText().toString();
                handleHelplineNumberClick(helplineNumber);
            }
        });

        textViewHelplineNumber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String helplineNumber = ((TextView) v).getText().toString();
                handleHelplineNumberClick(helplineNumber);
            }
        });
    }

    private void handleHelplineNumberClick(String helplineNumber) {
        switch (helplineNumber.trim()) {
            case "Police":
                dialPhoneNumber("100");
                break;
            case "Ambulance":
                dialPhoneNumber("102");
                break;
            case "Fire":
                dialPhoneNumber("101");
                break;
            // Add more cases for other helpline numbers if needed
            default:
                Log.d("HelplineActivity", "Clicked helpline number: " + helplineNumber);
        }
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}

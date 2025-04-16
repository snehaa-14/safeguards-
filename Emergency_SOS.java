package com.example.safeguard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.location.Location;
import android.Manifest;
import android.content.Intent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.view.KeyEvent;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import java.util.List;

import android.telephony.SmsManager;

public class Emergency_SOS extends AppCompatActivity {
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    private boolean volumeButtonPressed = false;
    private boolean powerButtonPressed = false;
    private boolean locationSent = false;
    private DatabaseHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_sos);
        dbHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeButtonPressed = true;
            checkAndSendLocation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            powerButtonPressed = true;
            checkAndSendLocation();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void checkAndSendLocation() {
        if (volumeButtonPressed && powerButtonPressed && !locationSent) {
            // Send live location to contacts
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request the last known location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Handle location found
                                sendLiveLocationToContacts(location);
                            } else {
                                showToast("Unable to retrieve location.");
                            }
                        }
                    });
        } else {
            // Location permissions not granted, request them
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }

    private void sendLiveLocationToContacts(Location location) {
        // Set location sent flag to prevent multiple sends
        locationSent = true;

        // Retrieve contacts from the database
        List<Contact> contacts = dbHelper.getAllContacts();

        // Check if there are any contacts available
        if (contacts != null && !contacts.isEmpty()) {
            // Iterate over the retrieved contacts
            for (Contact contact : contacts) {
                // Obtain contact information (e.g., phone numbers)
                String phoneNumber1 = contact.getContact1();
                String phoneNumber2 = contact.getContact2();

                // Check if location is available
                if (location != null) {
                    // Send live location to the first contact via SMS
                    sendLocationViaSMS(phoneNumber1, location);
                    // Send live location to the second contact via SMS
                    sendLocationViaSMS(phoneNumber2, location);
                } else {
                    showToast("Unable to fetch device location.");
                }
            }
        } else {
            showToast("No contacts available.");
        }
    }

    private void sendLocationViaSMS(String phoneNumber, Location location) {
        // Construct the message containing the live location
        String message = "Here is my live location: " +
                "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();

        // Use SmsManager to send the message
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        showToast("Location sent successfully by Voice Activation to " + phoneNumber);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

package com.example.safeguard;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import android.speech.RecognizerIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;

import android.widget.ImageButton;
import android.content.Intent;
import android.view.View;
import java.util.List;
import android.telephony.SmsManager;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Locale;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class Main_module_dis extends AppCompatActivity implements SensorEventListener {
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int shakeCount = 0;
    private boolean isShaken = false;
    private boolean locationSent = false;
    private DatabaseHelper dbHelper;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private FusedLocationProviderClient fusedLocationClient;

    // SOS feature variables
    private static final int PERMISSION_REQUEST_SMS = 1;
    private boolean volumeUpPressed = false;
    private boolean volumeDownPressed = false;
    private String emergencyPhoneNumber;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 101;
    private static final String SOS_PHRASE = "send help";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_module_dis);
        ImageView logoutButton = findViewById(R.id.buttonLogout);
        ImageView sosButton = findViewById(R.id.buttonsos);
        String username = getIntent().getStringExtra("USERNAME");
        if (username != null) {
            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case when username is null
            // For example, show a default welcome message
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        }

        // Check and request location permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }

        // Check if the app was opened by voice command
        handleVoiceCommand(getIntent());
        sosButton.setOnClickListener(v -> startVoiceInput());

        imageView1=findViewById(R.id.im1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the desired activity
                Intent intent = new Intent(Main_module_dis.this, helpline.class);
                // Start the activity
                startActivity(intent);
            }
        });

        imageView2=findViewById(R.id.im2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the desired activity
                Intent intent = new Intent(Main_module_dis.this, Siren_Activity.class);

                // Start the activity
                startActivity(intent);
            }
        });

        imageView3=findViewById(R.id.im3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the desired activity
                Intent intent = new Intent(Main_module_dis.this, SafeTips.class);

                // Start the activity
                startActivity(intent);
            }
        });

        imageView4=findViewById(R.id.im4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the desired activity
                Intent intent = new Intent(Main_module_dis.this, About_Us.class);

                // Start the activity
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the main page (LoginActivity in this example)
                Intent intent = new Intent(Main_module_dis.this, Login_Activity.class);
                startActivity(intent);
                // Display a toast message
                Toast.makeText(Main_module_dis.this, "Logout successful. Please login to enter the app.", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize SOS feature
        initializeSOS();

        // Initialize shake detection
        initializeSensor();

        dbHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Permission granted, proceed with initialization
            initializeSensor();
        }
    }

    private void initializeSensor() {
        // Initialize sensor manager and accelerometer sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                showToast("Accelerometer not available on this device");
            }
        } else {
            showToast("Sensor manager not available on this device");
        }
    }

    private void initializeSOS() {
        // Request SMS permission for SOS feature
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SMS);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Handle shake detection
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double acceleration = Math.sqrt(x * x + y * y + z * z);

        if (acceleration > 12) { // Adjust the threshold according to your requirement
            shakeCount++; // Increment the shake count
            if (shakeCount == 3 && !isShaken && !locationSent) {
                // Send live location to contacts
                getCurrentLocation();
                isShaken = true;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this implementation
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
        // Reset shake count
        shakeCount = 0;
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
        String message = "I am in Trouble Here is my live location: " +
                "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();

        // Use SmsManager to send the message
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        showToast("Location sent successfully to " + phoneNumber);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Reset flags when activity is destroyed
        isShaken = false;
        locationSent = false;

        // Unregister sensor listener when activity is destroyed
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeUpPressed = true;
            checkForSOS();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeDownPressed = true;
            checkForSOS();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeUpPressed = false;
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeDownPressed = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void checkForSOS() {
        if (volumeUpPressed && volumeDownPressed && emergencyPhoneNumber != null && !emergencyPhoneNumber.isEmpty()) {
            sendSOS();
        }
    }

    private void sendSOS() {
        // Check for SMS permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send SOS message
        String message = "Emergency! I need help. My current location is: ";
        getCurrentLocationForSOS(message);
    }

    private void getCurrentLocationForSOS(final String message) {
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
                                sendSOSEmergencyMessage(message + "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude());
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

    private void sendSOSEmergencyMessage(String message) {
        // Use SmsManager to send the SOS message
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(emergencyPhoneNumber, null, message, null, null);
        showToast("SOS message sent!");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleVoiceCommand(intent);
    }

    private void handleVoiceCommand(Intent intent) {
        if (intent.hasExtra(RecognizerIntent.EXTRA_RESULTS)) {
            ArrayList<String> voiceResults = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (voiceResults != null && !voiceResults.isEmpty()) {
                String voiceCommand = voiceResults.get(0).toLowerCase();
                if (voiceCommand.contains(SOS_PHRASE)) {
                    // Check for location permission and then retrieve and send location
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        // Retrieve contacts from the database
                        List<Contact> contacts = dbHelper.getAllContacts();

                        // Check if there are any contacts available
                        if (contacts != null && !contacts.isEmpty()) {
                            // Iterate over the retrieved contacts
                            for (Contact contact : contacts) {
                                // Obtain contact information (e.g., phone numbers)
                                String phoneNumber1 = contact.getContact1();
                                String phoneNumber2 = contact.getContact2();

                                // Send SOS message to each contact
                                sendLiveLocation(phoneNumber1);
                                sendLiveLocation(phoneNumber2);
                            }
                        } else {
                            Toast.makeText(this, "No contacts available.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid voice command", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void startVoiceInput() {
        // Start voice recognition intent
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLiveLocation(String phoneNumber) {
        // Get the current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);

        // Check if location is available
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Construct the message with location details
            String message = "Help! I'm in danger. My current location is: " +
                    "https://maps.google.com/?q=" + latitude + "," + longitude;

            // Send SMS with the location details
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            Toast.makeText(this, "SOS message with location sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                handleVoiceCommand(data);
            }
        }
    }
}

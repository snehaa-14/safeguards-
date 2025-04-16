package com.example.safeguard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class Signup_Activity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, contact1EditText, contact2EditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        nameEditText = findViewById(R.id.editTextPersonName1);
        emailEditText = findViewById(R.id.editTextPersonEmail);
        passwordEditText = findViewById(R.id.editTextPersonpassword);
        contact1EditText = findViewById(R.id.editText_contact1);
        contact2EditText = findViewById(R.id.editText_contact2);

        // Initialize SignUp button and set OnClickListener
        Button signupButton = findViewById(R.id.signup_btn);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        // Get user-entered data from EditText fields
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String contact1 = contact1EditText.getText().toString().trim();
        String contact2 = contact2EditText.getText().toString().trim();

        // Check if any field is empty
        if (name.isEmpty()) {
            showToast("Name field is not filled");
            return;
        }
        if (email.isEmpty()) {
            showToast("Email field is not filled");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid email address");
            return;
        }
        if (password.isEmpty()) {
            showToast("Password field is not filled");
            return;
        }
        if (!isValidPassword(password)) {
            showToast("Password should contain 8 characters including 5 alphabets, 2 digits, and 1 symbol");
            return;
        }
        if (contact1.isEmpty()) {
            showToast("Contact 1 field is not filled");
            return;
        }

        if (contact2.isEmpty()) {
            showToast("Contact 2 field is not filled");
            return;
        }

        // Insert the user data into the database
        String username = dbHelper.addUser(name, email, password, contact1, contact2);

        if (username != null) {
            // User data inserted successfully
            showToast("Signup successful!");
            Intent intent = new Intent(Signup_Activity.this, Main_module_dis.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        } else {
            // Error occurred while inserting user data
            showToast("An error occurred. Please try again.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private boolean isValidPassword(String password) {
        // Password should contain 8 characters including 5 alphabets, 2 digits, and 1 symbol
        String regex = "^(?=.*[a-zA-Z]{5})(?=.*\\d{2})(?=.*[@#$%^&+=])(?=.*[a-zA-Z0-9]).{8}$";
        return password.matches(regex);
    }

}
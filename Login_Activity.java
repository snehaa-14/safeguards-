package com.example.safeguard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class Login_Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1001;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin, googlesignin;
    TextView textViewSignUp;
    DatabaseHelper dbHelper;


    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        // Extract account information
                        String username = account.getEmail(); // Assuming email is used as the username
                        String password = "google_sign_in_password"; // Generate a unique password for Google sign-in
                        // Perform login process
                        loginWithGoogleAccount(username, password);
                    } catch (ApiException e) {
                        // Handle sign-in failure
                        Toast.makeText(getApplicationContext(), "Google sign-in failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.PersonEmail);
        editTextPassword = findViewById(R.id.Personpassword);
        buttonLogin = findViewById(R.id.button3);
        textViewSignUp = findViewById(R.id.txt7);
        googlesignin = findViewById(R.id.googlebtn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googlesignin.setOnClickListener(v -> signInWithGoogle());

        buttonLogin.setOnClickListener(v -> loginUser());

        textViewSignUp.setOnClickListener(v -> startActivity(new Intent(Login_Activity.this, Signup_Activity.class)));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of Google Sign-In flow
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, retrieve user's email and proceed with login
            String userEmail = account.getEmail();
            String userPassword = ""; // You may set a default password for Google sign-in users or handle it differently
            loginWithGoogleAccount(userEmail, userPassword);
        } catch (ApiException e) {

        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }

    private void loginWithGoogleAccount(String username, String password) {
        // Check if the user exists in your database
        if (dbHelper.checkUsernameExists(username)) {
            // If the user exists, you can directly proceed with login
            // You may choose to authenticate the user using the provided password or directly log them in
            // Here, for simplicity, we're assuming that the user exists and authenticating them
            if (dbHelper.checkPassword(username, password)) {
                // If the password matches, log the user in
                String loggedInUsername = dbHelper.getUsername(username);
                Toast.makeText(this, "Welcome " + loggedInUsername, Toast.LENGTH_SHORT).show();
                saveLoginStatus(true);
                startActivity(new Intent(Login_Activity.this, Main_module_dis.class));
                finish();
            } else {
                // If the password doesn't match, display an error message
                Toast.makeText(this, "Invalid password for Google sign-in", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If the user doesn't exist, you can handle it based on your application logic
            // Here, for simplicity, we're redirecting the user to the sign-up activity
            Intent intent = new Intent(Login_Activity.this, Signup_Activity.class);
            intent.putExtra("username", username); // Pass the username to the sign-up activity
            startActivity(intent);
        }
    }

    private boolean checkCredentials(String username, String password) {
        // Check if the username exists in the database
        if (dbHelper.checkUsernameExists(username)) {
            // If the username exists, check if the password matches
            return dbHelper.checkPassword(username, password);
        } else {
            // If the username doesn't exist, suggest the user to sign up
            Toast.makeText(this, "User not found. Please sign up.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the credentials are valid
        if (checkCredentials(username, password)) {
            // If credentials are valid, proceed with login
            String loggedInUsername = dbHelper.getUsername(username);
            Toast.makeText(this, "Welcome " + loggedInUsername, Toast.LENGTH_SHORT).show();
            saveLoginStatus(true);
            startActivity(new Intent(Login_Activity.this, Main_module_dis.class));
            finish();
        } else {
            // If credentials are not valid, show an error message
            Toast.makeText(this, "Invalid email or password. Please sign up or check your credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}

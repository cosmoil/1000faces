package com.cosmo.thousandfaces.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cosmo.thousandfaces.R;
import com.cosmo.thousandfaces.controller.LoginController;

public class RegistrationActivity extends Activity {

    private EditText editUsername, editPassword, editConfirmPassword, editEmail;
    private Button registerBtn;
    private TextView loginLink;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize views
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        editEmail = findViewById(R.id.editEmail);
        registerBtn = findViewById(R.id.registerBtn);
        loginLink = findViewById(R.id.loginLink);

        loginController = new LoginController(this);

        // Register button click listener
        registerBtn.setOnClickListener(view -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString();
            String confirmPassword = editConfirmPassword.getText().toString();
            String email = editEmail.getText().toString().trim();

            // Validate input
            if (validateInput(username, password, confirmPassword, email)) {
                // Attempt registration
                if (loginController.registerUser(username, password, email)) {
                    Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();

                    // Return to login screen
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("registered_username", username);
                    startActivity(intent);
                    finish();
                } else {
                    String error = loginController.getRegistrationError(username, password, email);
                    Toast.makeText(this, error != null ? error : "Registration failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Login link click listener
        loginLink.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateInput(String username, String password, String confirmPassword, String email) {
        // Check if fields are empty
        if (username.isEmpty()) {
            editUsername.setError("Username is required");
            editUsername.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            editConfirmPassword.setError("Please confirm your password");
            editConfirmPassword.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError("Passwords do not match");
            editConfirmPassword.requestFocus();
            return false;
        }

        // Check password length
        if (password.length() < 6) {
            editPassword.setError("Password must be at least 6 characters long");
            editPassword.requestFocus();
            return false;
        }

        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            editEmail.setError("Please enter a valid email address");
            editEmail.requestFocus();
            return false;
        }

        return true;
    }
}
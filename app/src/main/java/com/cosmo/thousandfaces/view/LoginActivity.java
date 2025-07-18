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

public class LoginActivity extends Activity {

    private EditText editUsername, editPassword;
    private Button loginBtn;
    private TextView registerLink;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.registerLink);

        loginController = new LoginController(this);

        // Check if coming from registration
        String registeredUsername = getIntent().getStringExtra("registered_username");
        if (registeredUsername != null) {
            editUsername.setText(registeredUsername);
        }

        // Login button click listener
        loginBtn.setOnClickListener(view -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString();

            if (validateInput(username, password)) {
                if (loginController.validateLogin(username, password)) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                    // Start home activity
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    editPassword.setText(""); // Clear password field
                }
            }
        });

        // Register link click listener
        registerLink.setOnClickListener(view -> {
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();
        });
    }

    private boolean validateInput(String username, String password) {
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

        return true;
    }
}
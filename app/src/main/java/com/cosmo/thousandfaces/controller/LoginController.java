package com.cosmo.thousandfaces.controller;

import android.content.Context;
import com.cosmo.thousandfaces.database.DatabaseHelper;
import com.cosmo.thousandfaces.model.User;

public class LoginController {

    private DatabaseHelper dbHelper;

    public LoginController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Validate login credentials
    public boolean validateLogin(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        return dbHelper.validateLogin(username.trim(), password);
    }

    // Register a new user
    public boolean registerUser(String username, String password, String email) {
        if (username == null || password == null || email == null ||
                username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            return false;
        }

        // Basic email validation
        if (!isValidEmail(email)) {
            return false;
        }

        // Password strength validation (minimum 6 characters)
        if (password.length() < 6) {
            return false;
        }

        return dbHelper.registerUser(username.trim(), password, email.trim());
    }

    // Check if username exists
    public boolean userExists(String username) {
        return dbHelper.userExists(username);
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return dbHelper.emailExists(email);
    }

    // Basic email validation
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }

    // Get registration error message
    public String getRegistrationError(String username, String password, String email) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty";
        }

        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters long";
        }

        if (email == null || !isValidEmail(email)) {
            return "Please enter a valid email address";
        }

        if (userExists(username)) {
            return "Username already exists";
        }

        if (emailExists(email)) {
            return "Email already registered";
        }

        return null; // No error
    }
}
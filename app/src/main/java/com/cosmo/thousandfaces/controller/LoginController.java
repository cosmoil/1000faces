package com.cosmo.thousandfaces.controller;

import com.cosmo.thousandfaces.model.User;

public class LoginController {

    // Dummy user – später durch DB-Check ersetzen
    private final User dummyUser = new User("admin", "1234");

    public boolean validateLogin(String username, String password) {
        return dummyUser.getUsername().equals(username) &&
                dummyUser.getPassword().equals(password);
    }
}

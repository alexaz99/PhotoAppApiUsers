package com.alex.photoapp.users.ui.model;

/**
 * The class fields match the login request
 * fields that a user send in json document over http request.
 */
public class LoginUserRequestModel {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

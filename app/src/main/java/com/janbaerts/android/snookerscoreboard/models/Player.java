package com.janbaerts.android.snookerscoreboard.models;

import androidx.annotation.NonNull;

public class Player {

    private String firstname;
    private String lastname;
    private String email;

    public Player() {}

    public Player(String firstname, String lastname, String email) {
        setFirstname(firstname);
        setLastname(lastname);
        setEmail(email);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s, %s", getFirstname(), getLastname(), getEmail());
    }
}

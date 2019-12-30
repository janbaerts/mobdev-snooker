package com.janbaerts.android.snookerscoreboard.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Player implements Comparable<Player> {

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
        return String.format("%s %s (%s)", getFirstname(), getLastname(), getEmail());
    }

    public String getFullName() {
        return String.format("%s %s", getFirstname(), getLastname());
    }

    public String getAbbreviatedName() {
        return String.format("%s. %s", getFirstname().charAt(0), getLastname());
    }

    // TODO: Fix compareTo, equals and hashCode.
    @Override
    public int compareTo(Player p) {
        return this.getLastname().compareTo(p.getLastname());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Player otherPlayer = (Player) obj;
        if (getLastname().equalsIgnoreCase(otherPlayer.getLastname())) {
            if (getFirstname().equalsIgnoreCase(otherPlayer.getFirstname())) {
                if (getEmail().equalsIgnoreCase(otherPlayer.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public int hashCode() {
        return getEmail().length();
    }
}

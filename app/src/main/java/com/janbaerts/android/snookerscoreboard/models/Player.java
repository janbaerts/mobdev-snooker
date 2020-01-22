package com.janbaerts.android.snookerscoreboard.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.janbaerts.android.snookerscoreboard.data.FavouriteBall;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Player implements Comparable<Player> {

    // TODO: Add break list.
    // TODO: Add player statistics screen.
    private String firstname;
    private String lastname;
    private String email;
    private FavouriteBall favouriteBall;
    private List<Break> breakList;
    private List<Break> highBreakHistory;

    public Player() {
        breakList = new ArrayList<>();
        highBreakHistory = new ArrayList<>();
        favouriteBall = FavouriteBall.REDBALL;
    }

    // TODO: Adjust constructor for extra properties.
    public Player(String firstname, String lastname, String email, FavouriteBall favouriteBall) {
        this();
        setFirstname(firstname);
        setLastname(lastname);
        setEmail(email);
        setFavouriteBall(favouriteBall);
    }

    // Getters and Setters
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

    public FavouriteBall getFavouriteBall() {
        return favouriteBall;
    }

    public void setFavouriteBall(FavouriteBall favouriteBall) {
        this.favouriteBall = favouriteBall;
    }

    public List<Break> getBreakList() {
        return breakList;
    }

    public void setBreakList(List<Break> breakList) {
        this.breakList = breakList;
    }

    public List<Break> getHighBreakHistory() {
        return highBreakHistory;
    }

    public void setHighBreakHistory(List<Break> highBreakHistory) {
        this.highBreakHistory = highBreakHistory;
    }

    // Custom methods
    public void addBreakToList(Break newBreak) {
        this.breakList.add(newBreak);
    }

    @Exclude
    public Break getHighestBreak() {
        if (breakList.size() == 0)
            return null;

        int highestBreakIndex = 0;
        for (int i = 1; i < breakList.size(); i++) {
            if (breakList.get(i).getTotalPoints() > breakList.get(highestBreakIndex).getTotalPoints())
                highestBreakIndex = i;
        }
        return breakList.get(highestBreakIndex);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s (%s)", getFirstname(), getLastname(), getEmail());
    }

    @Exclude
    public String getFullName() {
        return String.format("%s %s", getFirstname(), getLastname());
    }

    @Exclude
    public String getAbbreviatedName() {
        return String.format("%s. %s", getFirstname().charAt(0), getLastname());
    }

    @Exclude
    public String getDisplayName(Player otherPlayer) {
        if (this.getFirstname().equals(otherPlayer.getFirstname()))
            return getAbbreviatedName();
        else
            return getFirstname();
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

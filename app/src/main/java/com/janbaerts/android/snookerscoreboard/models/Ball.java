package com.janbaerts.android.snookerscoreboard.models;

public enum Ball {
    RED("a red", 1, 4),
    YELLOW("the yellow", 2, 4),
    GREEN("the green", 3, 4),
    BROWN("the brown", 4, 4),
    BLUE("the blue", 5, 5),
    PINK("the pink", 6, 6),
    BLACK("the black", 7, 7),
    WHITE("the cue ball", 0, 4);

    private String name;
    private String description;
    private int points;
    private int foulPoints;

    Ball(String description, int points, int foulPoints) {
        this.description = description;
        this.points = points;
        this.foulPoints = foulPoints;
    }

    public int getPoints() {
        return this.points;
    }

    public int getFoulPoints() {
        return this.foulPoints;
    }

    public String getDescription() { return this.description; }
}

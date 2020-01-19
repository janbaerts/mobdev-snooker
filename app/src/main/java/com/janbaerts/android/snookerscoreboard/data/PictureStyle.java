package com.janbaerts.android.snookerscoreboard.data;

public enum PictureStyle {
    CUEBALL(0, "cueball"),
    REDBALL(1, "redball"),
    YELLOWBALL(2, "yellowball"),
    GREENBALL(3, "greenball"),
    BROWNBALL(4, "brownball"),
    BLUEBALL(5, "blueball"),
    PINKBALL(6, "pinkball"),
    BLACKBALL(7, "blackball"),
    CUSTOM(8, "custom");

    private int value;
    private String name;

    PictureStyle(int value, String name) {
        this.value = value;
        this.name = name;
    }
}

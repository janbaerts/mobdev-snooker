package com.janbaerts.android.snookerscoreboard.data;

import com.janbaerts.android.snookerscoreboard.R;

public enum FavouriteBall {
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

    FavouriteBall(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getResource() {
        switch (value) {
            case 0:
                return R.drawable.cueball;
            case 1:
                return R.drawable.redball;
            case 2:
                return R.drawable.yellowball;
            case 3:
                return R.drawable.greenball;
            case 4:
                return R.drawable.brownball;
            case 5:
                return R.drawable.blueball;
            case 6:
                return R.drawable.pinkball;
            case 7:
                return R.drawable.blackball;
        }
        return 1;
    }
}

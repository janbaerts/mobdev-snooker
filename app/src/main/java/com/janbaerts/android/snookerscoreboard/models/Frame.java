package com.janbaerts.android.snookerscoreboard.models;

import java.util.List;

public class Frame {

    private static final int NUMBER_OF_REDS = 15;
    private int[] score;
    // TODO: remainingBalls should be a stack.
    private List<Ball> remainingBalls;

    public Frame() {
        score = new int[0];
        // TODO: Initialize remainingBalls.
    }

    // TODO: Come up with elegant scorekeeping.

    public int getScore(int playerIndex) {
        return score[playerIndex];
    }

    public void setScore(int playerIndex, int points) {
        this.score[playerIndex] += points;
    }

    public int getMaximumRemainingPoints(Break currentBreak) {
        return 0;
    }
}

package com.janbaerts.android.snookerscoreboard.models;

import com.janbaerts.android.snookerscoreboard.data.GameEventType;

import java.util.List;

public class Frame {

    private static final int NUMBER_OF_REDS = 15;

    private int[] score;
    private int remainingBalls;
    private int turn;

    private boolean isDownToTheColors;

    private List<GameEventType> events;
    private List<Break> breaks;

    public Frame() {
        score = new int[0];
        remainingBalls = 6 + NUMBER_OF_REDS;
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

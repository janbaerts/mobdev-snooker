package com.janbaerts.android.snookerscoreboard.models;

import com.janbaerts.android.snookerscoreboard.data.GameEventType;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    private static final int NUMBER_OF_REDS = 15;

    private int[] score;
    private int remainingBalls;
    private int turn;

    private boolean isDownToTheColors;

    private List<GameEvent> events;
    private List<Break> breaks;

    public Frame() {
        score = new int[0];
        remainingBalls = 6 + NUMBER_OF_REDS;
        breaks = new ArrayList<>();
        events = new ArrayList<>();
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

    public List<Break> getBreaks() {
        return this.breaks;
    }

    public void pushBreak(Break aBreak) {
        this.breaks.add(aBreak);
    }

    public Break popBreak() {
        return this.breaks.remove(this.breaks.size() - 1);
    }

    public List<GameEvent> getEvents() {
        return events;
    }

    public void pushEvent(GameEvent gameEvent) {
        this.events.add(gameEvent);
    }

    public GameEvent popEvent() {
        return this.events.remove(this.events.size() - 1);
    }

    public GameEvent[] getLastEvents(int numberOfEvents) {
        if (numberOfEvents > this.events.size())
            numberOfEvents = this.events.size();
        int lastIndex = this.events.size() - 1;
        GameEvent[] result = new GameEvent[numberOfEvents];
        for (int i = 0; i < numberOfEvents; i++)
            result[numberOfEvents - 1 - i] = this.events.get(lastIndex - i);
        return result;
    }
}

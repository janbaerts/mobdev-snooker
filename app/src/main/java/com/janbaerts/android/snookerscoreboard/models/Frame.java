package com.janbaerts.android.snookerscoreboard.models;

import com.janbaerts.android.snookerscoreboard.data.GameEventType;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    private static final int NUMBER_OF_REDS = 15;

    private int[] score;
    private int remainingBalls;
    private int turn;

    private List<GameEvent> events;
    private List<Break> breaks;

    public Frame(int startingPlayer) {
        score = new int[2];
        remainingBalls = 6 + NUMBER_OF_REDS;
        breaks = new ArrayList<>();
        events = new ArrayList<>();
        turn = startingPlayer;
    }

    public int getScore(int playerIndex) {
        return score[playerIndex];
    }
    public int[] getScore() { return this.score; }

    public void addPoints(int playerIndex, int points) {
        this.score[playerIndex] += points;
    }

    public void subtractScore(int playerIndex, int points) {
        this.score[playerIndex] -= points;
    }

    public void addBall() { this.remainingBalls++; }
    public void removeBall() { this.remainingBalls--; }
    public int getRemainingBalls() { return this.remainingBalls; }

    public int getMaximumRemainingPoints(Break currentBreak) {
        int remainingPoints = 0;
        if (remainingBalls > 6) {
            remainingPoints = ((remainingBalls - 6) * 8) + 27;
            if (currentBreak.getTotalPoints() != 0 && currentBreak.getLastBall() == Ball.RED)
            {
                remainingPoints += 7;
            }
        } else {
            for (int i = remainingBalls; i > 0; i--)
                remainingPoints += 8 - i;
        }
        return remainingPoints;
    }

    public List<Break> getBreaks() {
        return this.breaks;
    }

    public void pushBreak(Break aBreak) {
        this.breaks.add(aBreak);
    }

    public Break popBreak() {
        if (this.breaks.size() > 0)
            return this.breaks.remove(this.breaks.size() - 1);
        else
            return null;
    }

    public Break getLastBreak() {
        return this.breaks.get(this.breaks.size() - 1);
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

    public int getTurn() { return turn; }
    public void nextTurn() { this.turn++; }
    public void revertTurn() { this.turn--; }
}

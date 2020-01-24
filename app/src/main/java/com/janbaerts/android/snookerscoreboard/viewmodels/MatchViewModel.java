package com.janbaerts.android.snookerscoreboard.viewmodels;

import androidx.lifecycle.ViewModel;

import com.janbaerts.android.snookerscoreboard.models.Break;
import com.janbaerts.android.snookerscoreboard.models.Frame;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.Date;
import java.util.Random;

public class MatchViewModel extends ViewModel {

    private Date startingDateTime;

    private int currentFrame;
    private int matchStarter;
    private int maxNumberOfFrames;
//    private int turn;
    // TODO: Pass on data via Intent.

    private int[] score;
    private Frame[] frames;
    private Player[] players;
    private Break currentBreak;


    // Constructors
    public MatchViewModel() { }

    public void initialize(int maxNumberOfFrames) {
        this.maxNumberOfFrames = maxNumberOfFrames;
        setStartingDateTime();
        matchStarter = (new Random()).nextInt(100) % 2;
        currentFrame = 0;
        players = new Player[2];
        frames = new Frame[this.maxNumberOfFrames];
        frames[currentFrame] = new Frame(matchStarter);
        score = new int[2];
        score[0] = 0;
        score[1] = 0;
//        setCurrentBreak(new Break());
    }


    // Getters and setters
    public void setStartingDateTime() {
        this.startingDateTime = new Date();
        System.out.println(startingDateTime.toString());
        // TODO: For debugging purposes.
    }


    public Frame getCurrentFrame() {
        if (frames != null)
            return frames[currentFrame];
        return null;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public Break getCurrentBreak() {
        return currentBreak;
    }

    public void setCurrentBreak(Break currentBreak) {
        this.currentBreak = currentBreak;
    }

    public int getMatchStarter() {
        return matchStarter;
    }

    public void setMatchStarter(int matchStarter) {
        this.matchStarter = matchStarter;
    }

    public int getMaxNumberOfFrames() {
        return maxNumberOfFrames;
    }

    public void setMaxNumberOfFrames(int maxNumberOfFrames) {
        this.maxNumberOfFrames = maxNumberOfFrames;
    }

    // TODO: Clean up getScore and setScore and calculate it from frames.
    public String getScore() {
        return String.format("%d (%d) %d", score[0], maxNumberOfFrames, score[1]);
    }

    public void setScore(int[] score) {
        this.score = score;
    }

    public Frame[] getFrames() {
        return frames;
    }

    public void setFrames(Frame[] frames) {
        this.frames = frames;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public int getTurn() { return frames[currentFrame].getTurn(); }

    public void nextTurn() {
        // TODO: Decide conditions for break-saving.
        if (currentBreak.getTotalPoints() > 50)
            players[getPlayerAtTableIndex()].addBreakToList(currentBreak);

        frames[currentFrame].nextTurn();
        currentBreak = new Break(players[getPlayerAtTableIndex()]);
    }

    public void revertTurn() { frames[currentFrame].revertTurn(); }

    public int getPlayerAtTableIndex() { return getTurn() % 2; }

    public int getPlayerInSeatIndex() { return (getTurn() + 1) % 2; }

}

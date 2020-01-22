package com.janbaerts.android.snookerscoreboard.viewmodels;

import androidx.lifecycle.ViewModel;

import com.janbaerts.android.snookerscoreboard.models.Frame;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MatchViewModel extends ViewModel {

    private Date startingDateTime;

    private int currentFrame;
    private int matchStarter;
    private int maxNumberOfFrames;
    // TODO: Pass on data via Intent.

    private int[] score;
    private Frame[] frames;
    private Player[] players;


    // Constructors
    public MatchViewModel(int maxNumberOfFrames, Player firstPlayer, Player secondPlayer) {
        this.maxNumberOfFrames = maxNumberOfFrames;
        frames = new Frame[this.maxNumberOfFrames];
        players = new Player[2];
        players[0] = firstPlayer;
        players[1] = secondPlayer;
        matchStarter = (new Random()).nextInt() % 2;
        currentFrame = 0;
        frames[currentFrame] = new Frame();
    }




    // Getters and setters
    public void setStartingDateTime() {
        this.startingDateTime = new Date();
        System.out.println(startingDateTime.toString());
        // TODO: For debugging purposes.
    }


    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
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
    public int[] getScore() {
        return score;
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


}

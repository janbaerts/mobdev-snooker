package com.janbaerts.android.snookerscoreboard.viewmodels;

import android.widget.Toast;

import androidx.annotation.NonNull;
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

    private int[] score;
    private Frame[] frames;
    private Player[] players;
    private Break currentBreak;

    private boolean hasToBeInitialized = true;


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
//        score[0] = 0;
//        score[1] = 0;
        hasToBeInitialized = false;
//        setCurrentBreak(new Break());
    }

    public Player endFrame(Player frameLeader) {
        score[getPlayerIndex(frameLeader)]++;
        Player matchWinner = getMatchWinner();
        if (matchWinner != null) {
            return matchWinner;
        } else {
            cleanUpLastFrame();
            currentFrame++;
            frames[currentFrame] = new Frame((matchStarter + currentFrame) % 2);
            setCurrentBreak(new Break(players[(matchStarter + currentFrame) % 2]));
        }
        return null;
    }

    private void cleanUpLastFrame() {
        // TODO: Save frame statistics.

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

    public int getNumberOfFramesToWin() {
        return (getMaxNumberOfFrames() / 2) + 1;
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

    public int getPlayerIndex(Player player) {
        if (player.getEmail().equals(players[0].getEmail()))
            return 0;
        else
            return 1;
    }

    public Player getPlayer(int playerIndex) {
        return players[playerIndex];
    }

    public Player getFrameLeader() {
        int[] frameScore = getCurrentFrame().getScore();
        if (frameScore[0] > frameScore[1]) {
            return players[0];
        } else if (frameScore[0] < frameScore[1]) {
            return players[1];
        } else return null;
    }

    public Player getMatchWinner() {
        if (score[0] == getNumberOfFramesToWin())
            return players[0];
        if (score[1] == getNumberOfFramesToWin())
            return players[1];
        return null;
    }

    public int getTurn() { return frames[currentFrame].getTurn(); }

    public void nextTurn() {
        // TODO: Decide conditions for break-saving and implement.

        frames[currentFrame].nextTurn();
        currentBreak = new Break(players[getPlayerAtTableIndex()]);
    }

    public void revertTurn() { frames[currentFrame].revertTurn(); }

    public int getPlayerAtTableIndex() { return getTurn() % 2; }

    public int getPlayerInSeatIndex() { return (getTurn() + 1) % 2; }

    public boolean getHasToBeInitialized() { return hasToBeInitialized; }

//    @NonNull
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("MatchViewModel description:\n======================================\n");
//        sb.append(String.format("Player 1 = %s%n", getPlayers()[0].getFullName()));
//        sb.append(String.format("Player 2 = %s%n", getPlayers()[1].getFullName()));
//        sb.append(String.format("FrameScore = %s", getScore()));
//        sb.append(getCurrentFrame().toString());
//
//        return sb.toString();
//    }
}

package com.janbaerts.android.snookerscoreboard.viewmodels;

import androidx.lifecycle.ViewModel;

import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.List;

public class MatchSettingsViewModel extends ViewModel {

    private Player[] players = new Player[2];
    private Player selectedPlayer;
    private List<Player> playerList;
    private int maxNumberOfFrames;
    private int indexOfPlayerBeingSelected;

    public int getIndexOfPlayerBeingSelected() {
        return indexOfPlayerBeingSelected;
    }

    public void setIndexOfPlayerBeingSelected(int indexOfPlayerBeingSelected) {
        this.indexOfPlayerBeingSelected = indexOfPlayerBeingSelected;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setSelectedPlayer(Player selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }

    public int getMaxNumberOfFrames() {
        return maxNumberOfFrames;
    }

    public void setMaxNumberOfFrames(int maxNumberOfFrames) {
        this.maxNumberOfFrames = maxNumberOfFrames;
    }

    public void setPlayer(Player player, int playerIndex) {
        players[playerIndex] = player;
    }

    public void setQueryResult(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Player> getQueryResult() {
        return this.playerList;
    }
}

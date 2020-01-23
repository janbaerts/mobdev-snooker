package com.janbaerts.android.snookerscoreboard.services;

import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.List;

public interface PlayerRepositoryInterface {

    List<Player> getAllPlayers(boolean refresh);
    Player getFirstPlayer();
    Player getSecondPlayer();
    Player findPlayer(String playerId);

}

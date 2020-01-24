package com.janbaerts.android.snookerscoreboard.models;

import com.janbaerts.android.snookerscoreboard.data.GameEventType;

public class GameEvent {

    private GameEventType type;
    private Ball ball;
    private Player player; // Important: This is the player who receives points as a result of this GameEvent.
    private Break endedBreak;
    private String otherPlayerName;
    private int playerIndex;
    private int eventIndex;

    public GameEvent() { }

    public String toString() {
        switch (getType()) {
            // TODO: Make use of getDisplayName().
            case END_OF_TURN:
                return String.format("%d. End of turn for %s.", eventIndex, getPlayer().getFirstname());
            case POT:
                return String.format("%d. %s potted %s.", eventIndex, getPlayer().getFirstname(), getBall().getDescription());
            case FOUL:
                if (ball.getPoints() == 0)
                    return String.format("%d. %s went in-off.", eventIndex, otherPlayerName);
                return String.format("%d. %s made a foul on %s.", eventIndex, otherPlayerName, getBall().getDescription());
            case END_OF_BREAK:
                return String.format("%d. %s made a break of %d.", eventIndex, getPlayer().getFirstname(), getEndedBreak().getTotalPoints());
            case END_OF_BREAK_FOUL:
                if (ball.getPoints() == 0)
                    return String.format("%d. %s made a break of %d and went in-off.", eventIndex, otherPlayerName, getEndedBreak().getTotalPoints());
                return String.format("%d. %s made a break of %d and made a foul on %s.", eventIndex, otherPlayerName, getEndedBreak().getTotalPoints(),
                        getBall().getDescription());
        }
        return "In this event, there was but a strange void...";
    }


    public GameEventType getType() {
        return type;
    }

    public void setType(GameEventType type) {
        this.type = type;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getOtherPlayerName() {
        return otherPlayerName;
    }

    public void setOtherPlayerName(String otherPlayerName) {
        this.otherPlayerName = otherPlayerName;
    }

    public Break getEndedBreak() {
        return endedBreak;
    }

    public void setEndedBreak(Break endedBreak) {
        this.endedBreak = endedBreak;
    }

    public int getEventIndex() {
        return eventIndex;
    }

    public void setEventIndex(int eventIndex) {
        this.eventIndex = eventIndex;
    }
}

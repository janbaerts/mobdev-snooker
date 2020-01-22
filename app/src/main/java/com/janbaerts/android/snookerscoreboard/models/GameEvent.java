package com.janbaerts.android.snookerscoreboard.models;

import com.janbaerts.android.snookerscoreboard.R;
import com.janbaerts.android.snookerscoreboard.data.GameEventType;

public class GameEvent {

    private GameEventType type;
    private Ball ball;
    private Player player; // Important: This is the player who receives points as a result of this GameEvent.
    private Break endedBreak;

    public GameEvent(GameEventType type, Ball ball, Player player, Break endedBreak) {
        setType(type);
        setBall(ball);
        setPlayer(player);
        setEndedBreak(endedBreak);
    }

    @Override
    public String toString() {
        switch (getType()) {
            case END_OF_TURN:
                return String.format("End of turn for %s.", getPlayer().getFirstname());
            case POT:
                return String.format("%s potted %s.", getPlayer().getFirstname(), getBall().name());
            case FOUL:
                if (ball.getPoints() == 0)
                    return String.format("%s went in-off.", getPlayer().getFirstname());
                return String.format("%s made a foul on %s.", getPlayer().getFirstname(), getBall().name());
            case END_OF_BREAK:
                return String.format("%s made a break of %d.", getPlayer().getFirstname(), getEndedBreak().getTotalPoints());
            case END_OF_BREAK_FOUL:
                if (ball.getPoints() == 0)
                    return String.format("%s made a break of %d and went in-off.", getPlayer().getFirstname(), getEndedBreak().getTotalPoints());
                return String.format("%s made a break of %d and made a foul on %s.", getPlayer().getFirstname(), getEndedBreak().getTotalPoints(),
                        getBall().getFoulPoints());
        }
        return "In this event, there was nothing...";
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

    public Break getEndedBreak() {
        return endedBreak;
    }

    public void setEndedBreak(Break endedBreak) {
        this.endedBreak = endedBreak;
    }
}

package com.janbaerts.android.snookerscoreboard.data;

import android.util.Log;

import com.janbaerts.android.snookerscoreboard.models.Ball;
import com.janbaerts.android.snookerscoreboard.models.Break;
import com.janbaerts.android.snookerscoreboard.models.Frame;
import com.janbaerts.android.snookerscoreboard.models.GameEvent;

public class Undo {

    private GameEvent gameEvent;
    private Frame frame;
    private Break recoveringBreak;

    public Undo(GameEvent gameEvent, Frame frame) {
        this.gameEvent = gameEvent;
        this.frame = frame;

        if (gameEvent.getEndedBreak() != null)
            this.recoveringBreak = gameEvent.getEndedBreak();

    }

    public Break undo() {
        System.out.println("GameEvent index = " + gameEvent.getEventIndex());
        if (gameEvent.getEventIndex() == 0) {
            Log.w("UNDO", "There are no more events to undo.");
            return null;
        }

        // TODO: When less than 3 events left, the list should be emptied accordingly.
        if (gameEvent.getType() == GameEventType.END_OF_TURN) {
            frame.revertTurn();
            frame.popBreak();
            return recoveringBreak;
        }

        if (gameEvent.getType() == GameEventType.FOUL) {
            frame.subtractScore(gameEvent.getPlayerIndex(), gameEvent.getBall().getFoulPoints());
            frame.popBreak();
            frame.revertTurn();
            return recoveringBreak;
        }

        if (gameEvent.getType() == GameEventType.POT) {
            frame.subtractScore(gameEvent.getPlayerIndex(), gameEvent.getBall().getPoints());
            System.out.println("EndedBreak = " + gameEvent.getEndedBreak().toString());
            gameEvent.getEndedBreak().popPot();
            if (gameEvent.getBall() == Ball.RED)
                frame.addBall();
            return recoveringBreak;
        }

        if (gameEvent.getType() == GameEventType.END_OF_BREAK) {
            frame.revertTurn();
            frame.popBreak();
            return recoveringBreak;
        }

        if (gameEvent.getType() == GameEventType.END_OF_BREAK_FOUL) {
            frame.subtractScore(gameEvent.getPlayerIndex(), gameEvent.getBall().getFoulPoints());
            frame.revertTurn();
            frame.popBreak();
            return recoveringBreak;
        }

        return null;
    }

}

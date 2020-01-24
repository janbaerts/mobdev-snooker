package com.janbaerts.android.snookerscoreboard.models;

import java.util.ArrayList;
import java.util.List;

public class Break {
    private Player breakBuilder;
    private List<Ball> pottedBalls;

    public Break() { }

    public Break(Player player) {
        this.pottedBalls = new ArrayList<>();
        this.breakBuilder = player;
    }

    public Break(Player player, List<Ball> pottedBalls) {
        this.breakBuilder = player;
        this.pottedBalls = pottedBalls;
    }

    public int getTotalPoints() {
        int sum = 0;
        if (pottedBalls.size() != 0) {
            for (Ball b : pottedBalls)
                sum += b.getPoints();
        }
        return sum;
    }

    public void pushPot(Ball ball) {
        this.pottedBalls.add(ball);
    }

    public void popPot() {
        this.pottedBalls.remove(this.pottedBalls.size() - 1);
    }

    public Ball getLastBall() {
        return this.pottedBalls.get(this.pottedBalls.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current break = ");
        for (Ball b : pottedBalls)
            sb.append(b.getDescription() + " / ");
        return sb.toString();
    }

}

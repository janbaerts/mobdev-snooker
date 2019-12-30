package com.janbaerts.android.snookerscoreboard.models;

public class Break {
    private Player breakBuilder;
    private Ball[] pottedBalls;
    private int firstAvailableSlot;

    public Break() { }

    public Break(Player player) {
        this.pottedBalls = new Ball[38];
        this.breakBuilder = player;
        this.firstAvailableSlot = 0;
    }

    public Break(Player player, Ball[] balls) {
        this.breakBuilder = player;
        this.pottedBalls = balls;
        setFirstAvailableSlot();
    }

    public void setFirstAvailableSlot() {
        int firstNullIndex = 0;
        while (pottedBalls[firstNullIndex] != null)
            firstNullIndex++;
        firstAvailableSlot = firstNullIndex;
    }

    public int getFirstAvailableSlot() {
        return firstAvailableSlot;
    }
    public void incrementFirstAvailableSlot() { firstAvailableSlot++; }
    public void decrementFirstAvailableSlot() { firstAvailableSlot--; }

    public int getTotalPoints() {
        int sum = 0;
        for (int i = 0; i <= firstAvailableSlot; i++)
            sum += pottedBalls[i].getPoints();
        return sum;
    }

}

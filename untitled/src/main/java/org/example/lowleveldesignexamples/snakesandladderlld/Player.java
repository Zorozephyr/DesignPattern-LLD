package org.example.lowleveldesignexamples.snakesandladderlld;
public class Player {
    int playerId;
    int currentPosition;

    public Player(int playerId, int currentPosition) {
        this.playerId = playerId;
        this.currentPosition = currentPosition;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

}

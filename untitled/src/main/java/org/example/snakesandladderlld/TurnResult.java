package org.example.snakesandladderlld;

/**
 * Value object to encapsulate the result of a turn
 */
public class TurnResult {
    private final boolean hasWon;
    private final boolean shouldContinueTurn;
    private final int finalPosition;
    private final String message;

    public TurnResult(boolean hasWon, boolean shouldContinueTurn, int finalPosition, String message) {
        this.hasWon = hasWon;
        this.shouldContinueTurn = shouldContinueTurn;
        this.finalPosition = finalPosition;
        this.message = message;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public boolean shouldContinueTurn() {
        return shouldContinueTurn;
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public String getMessage() {
        return message;
    }
}

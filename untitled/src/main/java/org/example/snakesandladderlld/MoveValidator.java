package org.example.snakesandladderlld;

/**
 * Chain of Responsibility pattern for validating and calculating moves
 * Handles boundary checks, snake/ladder logic
 */
public class MoveValidator {

    /**
     * Calculates the new position after applying game rules
     */
    public int calculateNewPosition(Player player, int diceValue, Board board) {
        int currentPosition = player.getCurrentPosition();
        int targetPosition = currentPosition + diceValue;

        // Boundary validation - can't go beyond board size
        if (targetPosition > board.getSize()) {
            return currentPosition; // Stay at current position
        }

        // Apply snake/ladder logic
        Cell targetCell = board.getCells()[targetPosition - 1]; // Convert to 0-based index
        if (targetCell.getJump() != null) {
            return targetCell.getJump().getEnd();
        }

        return targetPosition;
    }
}

package org.example.snakesandladderlld;

/**
 * Strategy for handling turns when player rolls a 6
 * Allows consecutive turns until non-6 is rolled
 */
public class SixTurnStrategy implements TurnStrategy {
    private final MoveValidator moveValidator;

    public SixTurnStrategy() {
        this.moveValidator = new MoveValidator();
    }

    @Override
    public TurnResult executeTurn(Player player, int diceValue, Board board, Dice dice) {
        // Handle starting position with a 6
        if (player.getCurrentPosition() == 0 && diceValue == 6) {
            player.setCurrentPosition(1);
            return new TurnResult(false, true, 1, 
                "Player " + player.getPlayerId() + " starts at position 1");
        }

        int newPosition = moveValidator.calculateNewPosition(player, diceValue, board);
        player.setCurrentPosition(newPosition);

        boolean hasWon = newPosition == board.getSize();
        String message = "Player " + player.getPlayerId() + " is at position " + newPosition;

        // Continue turn only if not won and rolled a 6
        boolean shouldContinue = !hasWon && diceValue == 6;

        return new TurnResult(hasWon, shouldContinue, newPosition, message);
    }
}

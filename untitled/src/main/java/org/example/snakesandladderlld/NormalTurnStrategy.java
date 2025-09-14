package org.example.snakesandladderlld;

/**
 * Strategy for handling normal turns (non-six dice rolls)
 */
public class NormalTurnStrategy implements TurnStrategy {
    private final MoveValidator moveValidator;

    public NormalTurnStrategy() {
        this.moveValidator = new MoveValidator();
    }

    @Override
    public TurnResult executeTurn(Player player, int diceValue, Board board, Dice dice) {
        // Can't start without rolling a 6
        if (player.getCurrentPosition() == 0 && diceValue != 6) {
            return new TurnResult(false, false, player.getCurrentPosition(), 
                "Player " + player.getPlayerId() + " needs a 6 to start");
        }

        int newPosition = moveValidator.calculateNewPosition(player, diceValue, board);
        player.setCurrentPosition(newPosition);

        boolean hasWon = newPosition == board.getSize();
        String message = "Player " + player.getPlayerId() + " is at position " + newPosition;

        return new TurnResult(hasWon, false, newPosition, message);
    }
}

package org.example.snakesandladderlld;

/**
 * Strategy Pattern interface for handling different turn types
 */
public interface TurnStrategy {
    TurnResult executeTurn(Player player, int diceValue, Board board, Dice dice);
}

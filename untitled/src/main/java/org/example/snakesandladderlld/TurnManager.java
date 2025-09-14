package org.example.snakesandladderlld;

/**
 * Manages turn execution using Strategy Pattern
 * Decides which strategy to use based on dice value
 */
public class TurnManager {
    private final TurnStrategy normalTurnStrategy;
    private final TurnStrategy sixTurnStrategy;

    public TurnManager() {
        this.normalTurnStrategy = new NormalTurnStrategy();
        this.sixTurnStrategy = new SixTurnStrategy();
    }

    /**
     * Executes a complete turn for a player, handling consecutive 6s
     */
    public TurnResult executePlayerTurn(Player player, Board board, Dice dice) {
        TurnResult result;
        int diceValue;
        
        do {
            diceValue = dice.rollDice();
            System.out.println("Player " + player.getPlayerId() + " rolled: " + diceValue);
            
            TurnStrategy strategy = (diceValue == 6) ? sixTurnStrategy : normalTurnStrategy;
            result = strategy.executeTurn(player, diceValue, board, dice);
            
            System.out.println(result.getMessage());
            
            if (result.hasWon()) {
                break;
            }
            
        } while (result.shouldContinueTurn());
        
        return result;
    }
}

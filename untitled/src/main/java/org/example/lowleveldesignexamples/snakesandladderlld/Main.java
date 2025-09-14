package org.example.lowleveldesignexamples.snakesandladderlld;

import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced Main class demonstrating the refactored Snakes and Ladders game
 * with improved design patterns and reduced complexity
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("🐍🪜 Welcome to Snakes and Ladders! 🪜🐍");
        System.out.println("========================================");
        
        // Create game components using Builder-like pattern
        Dice dice = new Dice(1);
        Board board = new Board(100);
        
        // Create players
        List<Player> players = createPlayers();
        
        // Create and configure game
        Game game = new Game(board, dice, players);
        
        // You can add additional observers here
        // game.addObserver(new GameStatisticsObserver());
        
        try {
            // Start the game
            game.startGame();
            
            // Game finished
            System.out.println("========================================");
            System.out.println("🎉 Game completed successfully!");
            System.out.println("Final state: " + game.getGameState());
            
        } catch (Exception e) {
            System.err.println("❌ Game error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Factory method to create players
     */
    private static List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, 0));
        players.add(new Player(2, 0));
        
        System.out.println("👥 Created " + players.size() + " players");
        return players;
    }
}

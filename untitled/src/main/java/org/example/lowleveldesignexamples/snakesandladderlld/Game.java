package org.example.lowleveldesignexamples.snakesandladderlld;

import java.util.ArrayList;
import java.util.List;

/**
 * Refactored Game class using multiple design patterns:
 * - Observer Pattern for game events
 * - Strategy Pattern for turn management
 * - State Pattern for game state management
 */
public class Game {
    private final Board board;
    private final Dice dice;
    private final List<Player> players;
    private final List<GameObserver> observers;
    private final TurnManager turnManager;
    
    private GameState gameState;
    private Player winner;
    private int currentPlayerIndex;

    public Game(Board board, Dice dice, List<Player> players) {
        this.board = board;
        this.dice = dice;
        this.players = players;
        this.observers = new ArrayList<>();
        this.turnManager = new TurnManager();
        this.gameState = GameState.WAITING_TO_START;
        this.currentPlayerIndex = 0;
        
        // Add default logger observer
        this.observers.add(new GameLogger());
    }

    /**
     * Add an observer to watch game events
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove an observer
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify all observers of player move
     */
    private void notifyPlayerMove(Player player, int newPosition) {
        observers.forEach(observer -> observer.onPlayerMove(player, newPosition));
    }

    /**
     * Notify all observers of game start
     */
    private void notifyGameStart() {
        observers.forEach(GameObserver::onGameStart);
    }

    /**
     * Notify all observers of game end
     */
    private void notifyGameEnd(Player winner) {
        observers.forEach(observer -> observer.onGameEnd(winner));
    }

    /**
     * Notify all observers of turn start
     */
    private void notifyTurnStart(Player player) {
        observers.forEach(observer -> observer.onTurnStart(player));
    }

    /**
     * Start the game - much cleaner implementation
     */
    public void startGame() {
        if (gameState != GameState.WAITING_TO_START) {
            throw new IllegalStateException("Game is already in progress or finished");
        }

        gameState = GameState.IN_PROGRESS;
        notifyGameStart();

        while (gameState == GameState.IN_PROGRESS) {
            Player currentPlayer = players.get(currentPlayerIndex);
            notifyTurnStart(currentPlayer);
            
            TurnResult result = turnManager.executePlayerTurn(currentPlayer, board, dice);
            notifyPlayerMove(currentPlayer, result.getFinalPosition());

            if (result.hasWon()) {
                winner = currentPlayer;
                gameState = GameState.FINISHED;
                notifyGameEnd(winner);
                break;
            }

            // Move to next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    /**
     * Get current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Get the winner (null if game not finished)
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Get current player
     */
    public Player getCurrentPlayer() {
        if (gameState == GameState.IN_PROGRESS) {
            return players.get(currentPlayerIndex);
        }
        return null;
    }
}

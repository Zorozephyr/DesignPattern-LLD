package org.example.lowleveldesignexamples.snakesandladderlld;

/**
 * Observer Pattern interface for game events
 */
public interface GameObserver {
    void onPlayerMove(Player player, int newPosition);
    void onGameStart();
    void onGameEnd(Player winner);
    void onTurnStart(Player player);
}

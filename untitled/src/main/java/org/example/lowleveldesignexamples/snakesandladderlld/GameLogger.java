package org.example.lowleveldesignexamples.snakesandladderlld;

/**
 * Concrete Observer for logging game events
 */
public class GameLogger implements GameObserver {
    
    @Override
    public void onPlayerMove(Player player, int newPosition) {
        System.out.println("Player " + player.getPlayerId() + " moved to position " + newPosition);
    }

    @Override
    public void onGameStart() {
        System.out.println("🎮 Game started!");
    }

    @Override
    public void onGameEnd(Player winner) {
        System.out.println("🏆 Winner is Player " + winner.getPlayerId());
    }

    @Override
    public void onTurnStart(Player player) {
        System.out.println("🎲 Player " + player.getPlayerId() + "'s turn");
    }
}

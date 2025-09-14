package org.example.lowleveldesignexamples.snakesandladderlld;
public class Dice {
    int diceCount=1;
    int minValue=1;
    int maxValue=6;

    public Dice(int diceCount) {
        this.diceCount = diceCount;
    }

    public int rollDice() {
        // Calculate random number between minValue (1) and maxValue (6) inclusive
        // Math.random() generates a double between 0.0 (inclusive) and 1.0 (exclusive)
        // Multiply by (maxValue - minValue + 1) to get range [0, 6)
        // Add minValue to shift range to [1, 7)
        // Cast to int to truncate decimal, giving us [1, 6] inclusive
        int total = 0;
        for(int i=0;i<diceCount;i++){
            total += (int) (Math.random() * (maxValue - minValue + 1)) + minValue;
        }
        return total;
    }
}

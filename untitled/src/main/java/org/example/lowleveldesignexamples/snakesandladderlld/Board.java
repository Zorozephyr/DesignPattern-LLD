package org.example.lowleveldesignexamples.snakesandladderlld;
public class Board {
    Cell[] cells;

    public Board(int size) {
        this.cells = new Cell[size];
        for(int j=0;j<size;j++) {
            cells[j] = new Cell(null);
            if(Math.random()<0.01){
                // Create snakes (go backward) and ladders (go forward)
                int jumpEnd = (int)(Math.random() * size) + 1;
                // Ensure we don't jump to the same position
                if (jumpEnd != j + 1) {
                    cells[j].setJump(new Jump(j + 1, jumpEnd));
                }
            }
        }
    }
    
    public int getSize() {
        return cells.length;
    }

    public Cell[] getCells() {
        return cells;
    }
    
}

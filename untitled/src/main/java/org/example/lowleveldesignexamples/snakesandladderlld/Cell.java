package org.example.lowleveldesignexamples.snakesandladderlld;


public class Cell {
    Jump jump;

    public Cell(Jump jump) {
        this.jump = jump;
    }

    //Getter and Setter
    public Jump getJump() {
        return jump;
    }
    public void setJump(Jump jump) {
        this.jump = jump;
    }

    //if jump is not null then return jump.getEnd() else return -1
    public int getEnd() {
        if(jump != null) {
            return jump.getEnd();
        }
        return -1;
    }
}

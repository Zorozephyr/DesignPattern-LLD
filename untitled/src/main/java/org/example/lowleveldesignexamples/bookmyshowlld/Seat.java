package org.example.lowleveldesignexamples.bookmyshowlld;

import org.example.lowleveldesignexamples.bookmyshowlld.enums.SeatCategory;

public class Seat {
    int seatId;
    int row;
    SeatCategory seatCategory;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public SeatCategory getSeatCategory() {
        return seatCategory;
    }

    public void setSeatCategory(SeatCategory seatCategory) {
        this.seatCategory = seatCategory;
    }
}

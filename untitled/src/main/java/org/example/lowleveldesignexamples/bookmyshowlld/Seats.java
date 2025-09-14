package org.example.lowleveldesignexamples.bookmyshowlld;

public class Seats {
    SeatType seatType;
    int price;
    int seatNumber;

    public Seats(int seatNumber, SeatType seatType, int price) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public int getPrice() {
        return price;
    }

    public int getSeatNumber() {
        return seatNumber;
    }
}

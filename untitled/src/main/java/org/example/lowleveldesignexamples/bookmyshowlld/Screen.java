package org.example.lowleveldesignexamples.bookmyshowlld;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    int screenId;
    List<Seat> seats = new ArrayList<>();

    public int getScreenId(){
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Seat> getSeats(){
        return seats;
    }
}

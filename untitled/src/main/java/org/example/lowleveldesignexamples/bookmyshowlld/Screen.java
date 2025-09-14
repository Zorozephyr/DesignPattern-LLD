package org.example.lowleveldesignexamples.bookmyshowlld;

import java.util.Map;

public class Screen {
    Map<Integer,Seats> seatsMap;

    public Screen(Map<Integer, Seats> seatsMap) {
        this.seatsMap = seatsMap;
    }

    public Map<Integer, Seats> getSeatsMap() {
        return seatsMap;
    }
}

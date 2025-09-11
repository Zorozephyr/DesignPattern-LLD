package org.example.elevatorsystemlld;

import java.util.List;

public interface ElevatorSelectionStrategy {

    Elevator selectElevator(List<Elevator> elevators, ElevatorRequest request);
}

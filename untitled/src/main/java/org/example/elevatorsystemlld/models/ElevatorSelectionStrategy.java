package org.example.elevatorsystemlld.models;

import java.util.List;

public interface ElevatorSelectionStrategy {
    ElevatorController selectElevator(List<ElevatorController> elevators, int requestFloor, Direction direction);
}

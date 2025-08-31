package org.example.elevatorsystemlld.models;

import java.util.List;

public class SingleElevatorSelectionStrategy implements ElevatorSelectionStrategy{
    @Override
    public ElevatorController selectElevator(List<ElevatorController> elevatorControllers, int requestFloor, Direction direction) {
        return elevatorControllers.get(0);
    }
}

package org.example.lowleveldesignexamples.elevatorsystemlld;

import java.util.List;

public class NearestElevatorStrategy implements ElevatorSelectionStrategy{
    @Override
    public Elevator selectElevator(List<Elevator> elevators, ElevatorRequest request) {
        Elevator bestElevator = elevators.get(0);
        int minDist = Integer.MAX_VALUE;
        for(Elevator elevator: elevators){
            int distance = Math.abs(elevator.getCurrentFloor() - request.getFloor());
            if(elevator.getDirection() == request.getDirection() || elevator.getDirection()==Direction.IDLE){
                if(distance<minDist){
                    minDist = distance;
                    bestElevator=elevator;
                }
            }
        }
        return bestElevator;
    }
}

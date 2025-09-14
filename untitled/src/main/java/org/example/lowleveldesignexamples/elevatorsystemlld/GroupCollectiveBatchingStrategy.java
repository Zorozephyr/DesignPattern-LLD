package org.example.lowleveldesignexamples.elevatorsystemlld;

import java.util.List;
import java.util.PriorityQueue;

public class GroupCollectiveBatchingStrategy implements ElevatorSelectionStrategy {
    @Override
    public Elevator selectElevator(List<Elevator> elevators, ElevatorRequest request) {
        /*
        Matches Direction - Nearest
        FallBack on IDLE
        Least Busy
         */
        int minPenalty = Integer.MAX_VALUE;
        Elevator bestElevator = elevators.get(0);
        for(Elevator elevator: elevators){
            int penalty = getPenalty(elevator,request);
            if(penalty<minPenalty){
                minPenalty = penalty;
                bestElevator = elevator;
            }
        }
        return bestElevator;
    }

    private int getPenalty(Elevator elevator, ElevatorRequest request){
        int penalty = 0;
        int distance = Math.abs(elevator.getCurrentFloor()- request.getFloor());
        penalty+=distance*PenaltyValues.DISTANCE_PER_FLOOR;
        boolean sameDirectionMatches = elevator.getDirection() == request.getDirection();
        boolean goingUp = elevator.getDirection() == Direction.UP;
        if(sameDirectionMatches){
            penalty += directionAwarePenalty(elevator,request.getFloor(),goingUp);
        }
        else if(elevator.getDirection() == Direction.IDLE){
            penalty += PenaltyValues.IDLE_PENALTY;
        }
        else{
            penalty += PenaltyValues.OPPOSITE_DIRECTION;
            int oppositeQueue = goingUp?elevator.getDownQueueSize(): elevator.getUpQueueSize();
            penalty+=oppositeQueue*PenaltyValues.OPPOSITE_QUEUE_PENALTY;
        }
        return penalty;
    }

    private int directionAwarePenalty(Elevator elevator, int floor, boolean goingUp){
        PriorityQueue<Integer> queue = goingUp? elevator.getUpQueue():elevator.getDownQueue();
        if(queue.contains(floor)){
            return 0;
        }
        
        // Check if elevator has already passed the requested floor in the same direction
        int currentFloor = elevator.getCurrentFloor();
        if(goingUp && currentFloor > floor){
            // Elevator is going up but has already passed the requested floor
            return PenaltyValues.OPPOSITE_DIRECTION;
        }
        else if(!goingUp && currentFloor < floor){
            // Elevator is going down but has already passed the requested floor
            return PenaltyValues.OPPOSITE_DIRECTION;
        }
        
        // Check if any stops in the queue would cause us to pass by the requested floor
        if(goingUp && queue.peek() != null && queue.peek() > floor){
            for(int stop: queue){
                if(stop >= floor){
                    return PenaltyValues.PASS_BY_FLOOR;
                }
            }
        }
        else if(!goingUp && queue.peek() != null && queue.peek() < floor){
            for(int stop: queue){
                if(stop <= floor){
                    return PenaltyValues.PASS_BY_FLOOR;
                }
            }
        }

        return PenaltyValues.INSERT_PENALTY;
    }
}

package org.example.lowleveldesignexamples.elevatorsystemlld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElevatorController {
    List<Elevator> elevators = new ArrayList<>();
    private Map<Integer, List<FloorButton>> floorButtons = new HashMap<>();
    private ElevatorSelectionStrategy elevatorSelectionStrategy;
    public ElevatorController(int numElevators, int numFloors, ElevatorSelectionStrategy strategy) {
        this.elevatorSelectionStrategy = strategy;
        for(int i=0;i<numElevators;i++){
            Elevator elevator = new Elevator(i);
            for(int floor=0;floor<numFloors;floor++){
                FloorDisplay floorDisplay = new FloorDisplay(floor, elevator);
                floorButtons.computeIfAbsent(floor, k -> new ArrayList<>()).add(new FloorButton(floor, elevator, this));
                elevator.addObserver(floorDisplay);
            }
            elevators.add(elevator);
            Thread elevatorThread = new Thread(elevator);
            elevatorThread.start();
        }

    }

    public void submitRequest(ElevatorRequest request){
        //Assign Request to an elevator
        Elevator bestElevator = elevatorSelectionStrategy.selectElevator(elevators,request);
        bestElevator.addRequest(request.getFloor());
    }

    public void pressUpButtonAtFloor(int floor, int elevatorId){
        floorButtons.get(floor).get(elevatorId).pressUpButton();
    }
    public void pressDownButtonAtFloor(int floor, int elevatorId){
        floorButtons.get(floor).get(elevatorId).pressDownButton();
    }

    public void pressInsideButton(int floor,int elevatorId){
        InsidePanel insidePanel = elevators.get(elevatorId).getInsidePanel();
        insidePanel.pressFloorButton(floor);
    }

    public InsidePanel getInsidePanel(int elevatorId){
        return elevators.get(elevatorId).getInsidePanel();
    }
}

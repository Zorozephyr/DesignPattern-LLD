package org.example.elevatorsystemlld;

public class FloorDisplay implements ElevatorObserver{
    private int floor;
    private final Elevator elevator;

    public FloorDisplay(int floor, Elevator elevator) {
        this.floor = floor;
        this.elevator = elevator;
    }

    public void display(ElevatorEvent elevatorEvent){
        System.out.printf("Floor Display [%d] [Elevator %d]: Elevator Status- Floor %d (%s) [%s]%n", floor, elevator.getId(),elevatorEvent.getFloor(),elevatorEvent.getDirection(),elevatorEvent.getEventType());
    }

    @Override
    public void update(ElevatorEvent elevatorEvent) {
        if(elevatorEvent.getElevatorId() == elevator.getId()){
            display(elevatorEvent);
        }
    }
}

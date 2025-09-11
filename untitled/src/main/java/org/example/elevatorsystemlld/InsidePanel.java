package org.example.elevatorsystemlld;

public class InsidePanel implements ElevatorObserver{
    private final Elevator elevator;

    public InsidePanel(Elevator elevator) {
        this.elevator = elevator;
    }

    public void pressFloorButton(int floor){
        System.out.printf("Button pressed for inside panel for lift %d: Button pressed for floor %d%n", elevator.getId(),floor);
        elevator.addRequest(floor);
    }

    public void display(ElevatorEvent elevatorEvent){
        System.out.printf("Inside Panel Elevator [%d]: Current Floor %d(%s)%n", elevator.getId(),elevatorEvent.getFloor(),elevatorEvent.getEventType());
    }

    @Override
    public void update(ElevatorEvent elevatorEvent) {
        if(elevatorEvent.getElevatorId()==elevator.getId()){
            display(elevatorEvent);
        }
    }
}

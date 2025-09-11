package org.example.elevatorsystemlld;

public class FloorButton {
    private int floor;
    private final Elevator elevator;
    private ElevatorController elevatorController;
    public FloorButton(int floor, Elevator elevator, ElevatorController elevatorController) {
        this.floor = floor;
        this.elevator = elevator;
        this.elevatorController = elevatorController;
    }

    public void pressUpButton(){
        ElevatorRequest request = new ElevatorRequest(floor, Direction.UP);
        System.out.printf("Floor %d UP Button Pressed%n",floor);
        elevatorController.submitRequest(request);
    }
    public void pressDownButton(){
        ElevatorRequest request = new ElevatorRequest(floor, Direction.DOWN);
        System.out.printf("Floor %d DOWN Button Pressed%n",floor);
        elevatorController.submitRequest(request);
    }
}

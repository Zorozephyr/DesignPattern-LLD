package org.example.elevatorsystemlld.models;

public class Elevator {
    private int currentFloor;
    private Direction currentDirection;
    private ElevatorStatus status;
    private int elevatorId;

    public Elevator(int elevatorId, int initialFloor) {
        this.elevatorId = elevatorId;
        this.currentFloor = initialFloor;
        this.currentDirection = Direction.UP;
        this.status = ElevatorStatus.IDLE;
    }

    // Simple getters and setters - no business logic
    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public ElevatorStatus getStatus() {
        return status;
    }

    public void setStatus(ElevatorStatus status) {
        this.status = status;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "id=" + elevatorId +
                ", floor=" + currentFloor +
                ", direction=" + currentDirection +
                ", status=" + status +
                '}';
    }
}

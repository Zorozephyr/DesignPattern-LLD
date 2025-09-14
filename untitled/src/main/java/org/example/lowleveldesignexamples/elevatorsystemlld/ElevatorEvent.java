package org.example.lowleveldesignexamples.elevatorsystemlld;

public class ElevatorEvent {
    public enum Type {DOOR_OPEN, MOVING, IDLE, ARRIVED}
    private int floor;
    private final Direction direction;
    private int elevatorId;
    private final ElevatorEvent.Type eventType;

    public ElevatorEvent(int floor, Direction direction, int elevatorId, Type eventType) {
        this.floor = floor;
        this.direction = direction;
        this.elevatorId = elevatorId;
        this.eventType = eventType;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public Type getEventType() {
        return eventType;
    }
}

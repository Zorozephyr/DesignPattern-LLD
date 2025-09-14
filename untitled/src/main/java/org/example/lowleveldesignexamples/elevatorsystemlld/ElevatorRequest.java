package org.example.lowleveldesignexamples.elevatorsystemlld;

public class ElevatorRequest {
    private int floor;
    private Direction direction;
    private RequestType requestType;

    public ElevatorRequest(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
        this.requestType = RequestType.OUTSIDE;
    }

    public Direction getDirection() {
        return direction;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public int getFloor() {
        return floor;
    }
}

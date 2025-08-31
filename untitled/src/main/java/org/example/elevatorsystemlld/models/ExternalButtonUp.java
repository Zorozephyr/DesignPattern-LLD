package org.example.elevatorsystemlld.models;

public class ExternalButtonUp extends ExternalButton {

    public ExternalButtonUp(ExternalButtonDispatcher dispatcher, int currentFloor) {
        super(dispatcher, currentFloor);
    }

    @Override
    public void pressButton() {
        externalButtonDispatcher.submitExternalRequest(currentFloor, Direction.UP);
    }

    @Override
    public Direction getDirection() {
        return Direction.UP;
    }
}

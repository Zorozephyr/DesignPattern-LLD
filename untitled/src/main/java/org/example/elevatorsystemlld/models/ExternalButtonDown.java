package org.example.elevatorsystemlld.models;

public class ExternalButtonDown extends ExternalButton {

    public ExternalButtonDown(ExternalButtonDispatcher dispatcher, int currentFloor) {
        super(dispatcher, currentFloor);
    }

    @Override
    public void pressButton() {
        externalButtonDispatcher.submitExternalRequest(currentFloor, Direction.DOWN);
    }

    @Override
    public Direction getDirection() {
        return Direction.DOWN;
    }
}

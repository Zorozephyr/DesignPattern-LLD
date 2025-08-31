package org.example.elevatorsystemlld.models;

public abstract class ExternalButton {
    protected ExternalButtonDispatcher externalButtonDispatcher;
    protected int currentFloor;

    public ExternalButton(ExternalButtonDispatcher dispatcher, int currentFloor) {
        this.externalButtonDispatcher = dispatcher;
        this.currentFloor = currentFloor;
    }

    public abstract void pressButton();
    public abstract Direction getDirection();
}

package org.example.elevatorsystemlld.models;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private List<ExternalButton> externalButtons;
    private int floorNumber;

    public Floor(int floorNumber, int totalFloors, ExternalButtonDispatcher dispatcher) {
        this.floorNumber = floorNumber;
        this.externalButtons = new ArrayList<>();

        // Ground floor (floor 0 or 1) - only UP button
        if (floorNumber == 0) {
            externalButtons.add(new ExternalButtonUp(dispatcher, floorNumber));
        }
        // Top floor - only DOWN button
        else if (floorNumber == totalFloors) {
            externalButtons.add(new ExternalButtonDown(dispatcher, floorNumber));
        }
        // Middle floors - both UP and DOWN buttons
        else {
            externalButtons.add(new ExternalButtonUp(dispatcher, floorNumber));
            externalButtons.add(new ExternalButtonDown(dispatcher, floorNumber));
        }
    }

    public List<ExternalButton> getExternalButtons() {
        return externalButtons;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    // Convenience methods for button access
    public ExternalButton getUpButton() {
        return externalButtons.stream()
            .filter(button -> button.getDirection() == Direction.UP)
            .findFirst()
            .orElse(null);
    }

    public ExternalButton getDownButton() {
        return externalButtons.stream()
            .filter(button -> button.getDirection() == Direction.DOWN)
            .findFirst()
            .orElse(null);
    }
}

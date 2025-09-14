package org.example.lowleveldesignexamples.carrentalsystemlld.enums;

/**
 * Enum representing different types of vehicles available for rental.
 * Each type has specific characteristics and pricing models.
 */
public enum VehicleType {
    CAR("Car", "Standard passenger vehicle"),
    MOTORCYCLE("Motorcycle", "Two-wheeler vehicle"),
    TRUCK("Truck", "Heavy-duty cargo vehicle"),
    VAN("Van", "Multi-passenger or cargo vehicle");

    private final String displayName;
    private final String description;

    VehicleType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

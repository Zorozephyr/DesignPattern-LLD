package org.example.carrentalsystemlld.enums;

/**
 * Enum representing the current status of a vehicle in the rental system.
 * This helps track vehicle availability and operational state.
 */
public enum VehicleStatus {
    AVAILABLE("Available", "Vehicle is ready for rental"),
    RESERVED("Reserved", "Vehicle is currently booked"),
    MAINTENANCE("Under Maintenance", "Vehicle is being serviced"),
    RETIRED("Retired", "Vehicle is no longer in service");

    private final String displayName;
    private final String description;

    VehicleStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailableForRental() {
        return this == AVAILABLE;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

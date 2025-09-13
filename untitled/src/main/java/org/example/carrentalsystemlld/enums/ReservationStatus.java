package org.example.carrentalsystemlld.enums;

/**
 * Enum representing the lifecycle status of a vehicle reservation.
 * Tracks the progression from booking to completion.
 */
public enum ReservationStatus {
    SCHEDULED("Scheduled", "Reservation is confirmed and scheduled"),
    ACTIVE("Active", "Vehicle is currently rented out"),
    COMPLETED("Completed", "Rental period finished successfully"),
    CANCELLED("Cancelled", "Reservation was cancelled");

    private final String displayName;
    private final String description;

    ReservationStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isCompleted() {
        return this == COMPLETED || this == CANCELLED;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

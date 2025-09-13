package org.example.carrentalsystemlld.enums;

/**
 * Enum representing the status of payment processing.
 * Tracks payment lifecycle from initiation to completion.
 */
public enum PaymentStatus {
    PENDING("Pending", "Payment is being processed"),
    COMPLETED("Completed", "Payment was successful"),
    FAILED("Failed", "Payment processing failed"),
    REFUNDED("Refunded", "Payment was refunded");

    private final String displayName;
    private final String description;

    PaymentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSuccessful() {
        return this == COMPLETED;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == REFUNDED;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

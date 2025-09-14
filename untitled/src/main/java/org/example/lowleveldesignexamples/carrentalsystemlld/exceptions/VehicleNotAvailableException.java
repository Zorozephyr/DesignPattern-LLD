package org.example.lowleveldesignexamples.carrentalsystemlld.exceptions;

/**
 * Exception thrown when attempting to reserve a vehicle that is not available.
 */
public class VehicleNotAvailableException extends CarRentalException {
    
    public VehicleNotAvailableException(String vehicleId) {
        super("Vehicle with ID " + vehicleId + " is not available for rental");
    }
    
    public VehicleNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

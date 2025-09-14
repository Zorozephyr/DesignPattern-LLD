package org.example.lowleveldesignexamples.carrentalsystemlld.exceptions;

/**
 * Exception thrown for invalid reservation operations or data.
 */
public class InvalidReservationException extends CarRentalException {
    
    public InvalidReservationException(String message) {
        super(message);
    }
    
    public InvalidReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}

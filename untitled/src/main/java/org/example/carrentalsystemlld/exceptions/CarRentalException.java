package org.example.carrentalsystemlld.exceptions;

/**
 * Base exception class for all car rental system related exceptions.
 * Provides a foundation for specific domain exceptions.
 */
public class CarRentalException extends Exception {
    
    public CarRentalException(String message) {
        super(message);
    }
    
    public CarRentalException(String message, Throwable cause) {
        super(message, cause);
    }
}

package org.example.lowleveldesignexamples.carrentalsystemlld.model.vehicles;

import org.example.lowleveldesignexamples.carrentalsystemlld.enums.VehicleType;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.Vehicle;

/**
 * Car implementation of Vehicle.
 * Represents standard passenger cars with specific features and pricing.
 */
public class Car extends Vehicle {
    private boolean hasAirConditioning;
    private boolean hasGPS;
    private String transmissionType; // Manual, Automatic
    private int numberOfDoors;

    public Car(String vehicleId, String vehicleNumber, double dailyRentalCost,
               String model, String brand, int manufacturingYear, int seatingCapacity,
               String fuelType, boolean hasAirConditioning, boolean hasGPS,
               String transmissionType, int numberOfDoors) {
        super(vehicleId, vehicleNumber, VehicleType.CAR, dailyRentalCost,
              model, brand, manufacturingYear, seatingCapacity, fuelType);
        this.hasAirConditioning = hasAirConditioning;
        this.hasGPS = hasGPS;
        this.transmissionType = transmissionType;
        this.numberOfDoors = numberOfDoors;
    }

    @Override
    public String getVehicleFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("Car Features: ");
        features.append(seatingCapacity).append(" seats, ");
        features.append(numberOfDoors).append(" doors, ");
        features.append(transmissionType).append(" transmission, ");
        features.append(fuelType).append(" fuel");
        
        if (hasAirConditioning) {
            features.append(", Air Conditioning");
        }
        if (hasGPS) {
            features.append(", GPS Navigation");
        }
        
        return features.toString();
    }

    @Override
    public double calculateRentalCost(int days) {
        double baseCost = dailyRentalCost * days;
        double multiplier = 1.0;
        
        // Premium features increase cost
        if (hasAirConditioning) {
            multiplier += 0.1; // 10% increase
        }
        if (hasGPS) {
            multiplier += 0.05; // 5% increase
        }
        if ("Automatic".equalsIgnoreCase(transmissionType)) {
            multiplier += 0.15; // 15% increase
        }
        
        return baseCost * multiplier;
    }

    // Getters and Setters
    public boolean hasAirConditioning() {
        return hasAirConditioning;
    }

    public void setHasAirConditioning(boolean hasAirConditioning) {
        this.hasAirConditioning = hasAirConditioning;
    }

    public boolean hasGPS() {
        return hasGPS;
    }

    public void setHasGPS(boolean hasGPS) {
        this.hasGPS = hasGPS;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }
}

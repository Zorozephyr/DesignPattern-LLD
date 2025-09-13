package org.example.carrentalsystemlld.model.vehicles;

import org.example.carrentalsystemlld.enums.VehicleType;
import org.example.carrentalsystemlld.model.Vehicle;

/**
 * Van implementation of Vehicle.
 * Represents multi-passenger or cargo vehicles with specific features.
 */
public class Van extends Vehicle {
    private boolean isPassengerVan;
    private boolean hasCargoSpace;
    private boolean hasSlidingDoors;
    private double cargoVolume; // in cubic feet

    public Van(String vehicleId, String vehicleNumber, double dailyRentalCost,
              String model, String brand, int manufacturingYear, int seatingCapacity,
              String fuelType, boolean isPassengerVan, boolean hasCargoSpace,
              boolean hasSlidingDoors, double cargoVolume) {
        super(vehicleId, vehicleNumber, VehicleType.VAN, dailyRentalCost,
              model, brand, manufacturingYear, seatingCapacity, fuelType);
        this.isPassengerVan = isPassengerVan;
        this.hasCargoSpace = hasCargoSpace;
        this.hasSlidingDoors = hasSlidingDoors;
        this.cargoVolume = cargoVolume;
    }

    @Override
    public String getVehicleFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("Van Features: ");
        features.append(seatingCapacity).append(" seats, ");
        
        if (isPassengerVan) {
            features.append("Passenger van, ");
        }
        if (hasCargoSpace) {
            features.append(cargoVolume).append(" cu ft cargo space, ");
        }
        if (hasSlidingDoors) {
            features.append("Sliding doors, ");
        }
        
        features.append(fuelType).append(" fuel");
        
        return features.toString();
    }

    @Override
    public double calculateRentalCost(int days) {
        double baseCost = dailyRentalCost * days;
        double multiplier = 1.0;
        
        // Large seating capacity increases cost
        if (seatingCapacity > 8) {
            multiplier += 0.2; // 20% increase for large vans
        } else if (seatingCapacity > 5) {
            multiplier += 0.1; // 10% increase for medium vans
        }
        
        // Cargo space adds value
        if (hasCargoSpace && cargoVolume > 100) {
            multiplier += 0.15; // 15% increase for large cargo space
        }
        
        // Convenience features
        if (hasSlidingDoors) {
            multiplier += 0.05; // 5% increase
        }
        
        return baseCost * multiplier;
    }

    // Getters and Setters
    public boolean isPassengerVan() {
        return isPassengerVan;
    }

    public void setPassengerVan(boolean passengerVan) {
        isPassengerVan = passengerVan;
    }

    public boolean hasCargoSpace() {
        return hasCargoSpace;
    }

    public void setHasCargoSpace(boolean hasCargoSpace) {
        this.hasCargoSpace = hasCargoSpace;
    }

    public boolean hasSlidingDoors() {
        return hasSlidingDoors;
    }

    public void setHasSlidingDoors(boolean hasSlidingDoors) {
        this.hasSlidingDoors = hasSlidingDoors;
    }

    public double getCargoVolume() {
        return cargoVolume;
    }

    public void setCargoVolume(double cargoVolume) {
        this.cargoVolume = cargoVolume;
    }
}

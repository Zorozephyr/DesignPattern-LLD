package org.example.lowleveldesignexamples.carrentalsystemlld.model.vehicles;

import org.example.lowleveldesignexamples.carrentalsystemlld.enums.VehicleType;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.Vehicle;

/**
 * Truck implementation of Vehicle.
 * Represents heavy-duty vehicles with cargo capacity and specific features.
 */
public class Truck extends Vehicle {
    private double cargoCapacity; // in tons
    private boolean hasLoadingRamp;
    private String truckType; // Pickup, Box Truck, Flatbed, etc.
    private boolean requiresSpecialLicense;

    public Truck(String vehicleId, String vehicleNumber, double dailyRentalCost,
                String model, String brand, int manufacturingYear, int seatingCapacity,
                String fuelType, double cargoCapacity, boolean hasLoadingRamp,
                String truckType, boolean requiresSpecialLicense) {
        super(vehicleId, vehicleNumber, VehicleType.TRUCK, dailyRentalCost,
              model, brand, manufacturingYear, seatingCapacity, fuelType);
        this.cargoCapacity = cargoCapacity;
        this.hasLoadingRamp = hasLoadingRamp;
        this.truckType = truckType;
        this.requiresSpecialLicense = requiresSpecialLicense;
    }

    @Override
    public String getVehicleFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("Truck Features: ");
        features.append(cargoCapacity).append(" tons capacity, ");
        features.append(truckType).append(" type, ");
        features.append(seatingCapacity).append(" seats, ");
        features.append(fuelType).append(" fuel");
        
        if (hasLoadingRamp) {
            features.append(", Loading ramp");
        }
        if (requiresSpecialLicense) {
            features.append(", Special license required");
        }
        
        return features.toString();
    }

    @Override
    public double calculateRentalCost(int days) {
        double baseCost = dailyRentalCost * days;
        double multiplier = 1.0;
        
        // Cargo capacity affects pricing
        if (cargoCapacity > 5.0) {
            multiplier += 0.3; // 30% increase for heavy-duty trucks
        } else if (cargoCapacity > 2.0) {
            multiplier += 0.2; // 20% increase for medium trucks
        }
        
        // Special features increase cost
        if (hasLoadingRamp) {
            multiplier += 0.1; // 10% increase
        }
        if (requiresSpecialLicense) {
            multiplier += 0.15; // 15% increase
        }
        
        return baseCost * multiplier;
    }

    // Getters and Setters
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    public void setCargoCapacity(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }

    public boolean hasLoadingRamp() {
        return hasLoadingRamp;
    }

    public void setHasLoadingRamp(boolean hasLoadingRamp) {
        this.hasLoadingRamp = hasLoadingRamp;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public boolean requiresSpecialLicense() {
        return requiresSpecialLicense;
    }

    public void setRequiresSpecialLicense(boolean requiresSpecialLicense) {
        this.requiresSpecialLicense = requiresSpecialLicense;
    }
}

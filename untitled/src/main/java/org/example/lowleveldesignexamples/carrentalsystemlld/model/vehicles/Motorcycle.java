package org.example.lowleveldesignexamples.carrentalsystemlld.model.vehicles;

import org.example.lowleveldesignexamples.carrentalsystemlld.enums.VehicleType;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.Vehicle;

/**
 * Motorcycle implementation of Vehicle.
 * Represents two-wheeler vehicles with specific features and pricing.
 */
public class Motorcycle extends Vehicle {
    private int engineCapacity; // in CC
    private boolean hasHelmet;
    private String motorcycleType; // Sports, Cruiser, Standard, etc.

    public Motorcycle(String vehicleId, String vehicleNumber, double dailyRentalCost,
                     String model, String brand, int manufacturingYear,
                     String fuelType, int engineCapacity, boolean hasHelmet,
                     String motorcycleType) {
        super(vehicleId, vehicleNumber, VehicleType.MOTORCYCLE, dailyRentalCost,
              model, brand, manufacturingYear, 2, fuelType); // Always 2 seats
        this.engineCapacity = engineCapacity;
        this.hasHelmet = hasHelmet;
        this.motorcycleType = motorcycleType;
    }

    @Override
    public String getVehicleFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("Motorcycle Features: ");
        features.append(engineCapacity).append("CC engine, ");
        features.append(motorcycleType).append(" type, ");
        features.append(fuelType).append(" fuel");
        
        if (hasHelmet) {
            features.append(", Helmet included");
        }
        
        return features.toString();
    }

    @Override
    public double calculateRentalCost(int days) {
        double baseCost = dailyRentalCost * days;
        double multiplier = 1.0;
        
        // Engine capacity affects pricing
        if (engineCapacity > 500) {
            multiplier += 0.2; // 20% increase for high-capacity bikes
        } else if (engineCapacity > 250) {
            multiplier += 0.1; // 10% increase for mid-capacity bikes
        }
        
        // Sports bikes are premium
        if ("Sports".equalsIgnoreCase(motorcycleType)) {
            multiplier += 0.15; // 15% increase
        }
        
        return baseCost * multiplier;
    }

    // Getters and Setters
    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public boolean hasHelmet() {
        return hasHelmet;
    }

    public void setHasHelmet(boolean hasHelmet) {
        this.hasHelmet = hasHelmet;
    }

    public String getMotorcycleType() {
        return motorcycleType;
    }

    public void setMotorcycleType(String motorcycleType) {
        this.motorcycleType = motorcycleType;
    }
}

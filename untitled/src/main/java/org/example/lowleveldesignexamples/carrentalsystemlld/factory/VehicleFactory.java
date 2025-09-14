package org.example.lowleveldesignexamples.carrentalsystemlld.factory;

import org.example.lowleveldesignexamples.carrentalsystemlld.enums.VehicleType;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.Vehicle;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.vehicles.Car;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.vehicles.Motorcycle;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.vehicles.Truck;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.vehicles.Van;

/**
 * Factory class for creating different types of vehicles.
 * Implements Factory Pattern to encapsulate vehicle creation logic.
 * 
 * Design Pattern: Factory Pattern
 * Purpose: Centralize vehicle creation and allow easy addition of new vehicle types
 */
public class VehicleFactory {
    
    /**
     * Creates a vehicle based on the specified type with default configurations.
     */
    public static Vehicle createVehicle(VehicleType vehicleType, String vehicleId, 
                                       String vehicleNumber, double dailyRentalCost,
                                       String model, String brand, int manufacturingYear) {
        switch (vehicleType) {
            case CAR:
                return createDefaultCar(vehicleId, vehicleNumber, dailyRentalCost,
                                      model, brand, manufacturingYear);
            
            case MOTORCYCLE:
                return createDefaultMotorcycle(vehicleId, vehicleNumber, dailyRentalCost,
                                             model, brand, manufacturingYear);
            
            case TRUCK:
                return createDefaultTruck(vehicleId, vehicleNumber, dailyRentalCost,
                                        model, brand, manufacturingYear);
            
            case VAN:
                return createDefaultVan(vehicleId, vehicleNumber, dailyRentalCost,
                                      model, brand, manufacturingYear);
            
            default:
                throw new IllegalArgumentException("Unsupported vehicle type: " + vehicleType);
        }
    }
    
    /**
     * Creates a car with custom specifications.
     */
    public static Car createCar(String vehicleId, String vehicleNumber, double dailyRentalCost,
                               String model, String brand, int manufacturingYear, int seatingCapacity,
                               String fuelType, boolean hasAirConditioning, boolean hasGPS,
                               String transmissionType, int numberOfDoors) {
        return new Car(vehicleId, vehicleNumber, dailyRentalCost, model, brand,
                      manufacturingYear, seatingCapacity, fuelType, hasAirConditioning,
                      hasGPS, transmissionType, numberOfDoors);
    }
    
    /**
     * Creates a motorcycle with custom specifications.
     */
    public static Motorcycle createMotorcycle(String vehicleId, String vehicleNumber, double dailyRentalCost,
                                            String model, String brand, int manufacturingYear,
                                            String fuelType, int engineCapacity, boolean hasHelmet,
                                            String motorcycleType) {
        return new Motorcycle(vehicleId, vehicleNumber, dailyRentalCost, model, brand,
                             manufacturingYear, fuelType, engineCapacity, hasHelmet, motorcycleType);
    }
    
    /**
     * Creates a truck with custom specifications.
     */
    public static Truck createTruck(String vehicleId, String vehicleNumber, double dailyRentalCost,
                                   String model, String brand, int manufacturingYear, int seatingCapacity,
                                   String fuelType, double cargoCapacity, boolean hasLoadingRamp,
                                   String truckType, boolean requiresSpecialLicense) {
        return new Truck(vehicleId, vehicleNumber, dailyRentalCost, model, brand,
                        manufacturingYear, seatingCapacity, fuelType, cargoCapacity,
                        hasLoadingRamp, truckType, requiresSpecialLicense);
    }
    
    /**
     * Creates a van with custom specifications.
     */
    public static Van createVan(String vehicleId, String vehicleNumber, double dailyRentalCost,
                               String model, String brand, int manufacturingYear, int seatingCapacity,
                               String fuelType, boolean isPassengerVan, boolean hasCargoSpace,
                               boolean hasSlidingDoors, double cargoVolume) {
        return new Van(vehicleId, vehicleNumber, dailyRentalCost, model, brand,
                      manufacturingYear, seatingCapacity, fuelType, isPassengerVan,
                      hasCargoSpace, hasSlidingDoors, cargoVolume);
    }
    
    // Private helper methods for default configurations
    private static Car createDefaultCar(String vehicleId, String vehicleNumber, double dailyRentalCost,
                                       String model, String brand, int manufacturingYear) {
        return new Car(vehicleId, vehicleNumber, dailyRentalCost, model, brand,
                      manufacturingYear, 5, "Petrol", true, false, "Manual", 4);
    }
    
    private static Motorcycle createDefaultMotorcycle(String vehicleId, String vehicleNumber, double dailyRentalCost,
                                                     String model, String brand, int manufacturingYear) {
        return new Motorcycle(vehicleId, vehicleNumber, dailyRentalCost, model, brand,
                             manufacturingYear, "Petrol", 150, true, "Standard");
    }
    
    private static Truck createDefaultTruck(String vehicleId, String vehicleNumber, double dailyRentalCost,
                                           String model, String brand, int manufacturingYear) {
        return new Truck(vehicleId, vehicleNumber, dailyRentalCost, model, brand,
                        manufacturingYear, 3, "Diesel", 2.0, false, "Pickup", false);
    }
    
    private static Van createDefaultVan(String vehicleId, String vehicleNumber, double dailyRentalCost,
                                       String model, String brand, int manufacturingYear) {
        return new Van(vehicleId, vehicleNumber, dailyRentalCost, model, brand,
                      manufacturingYear, 8, "Petrol", true, true, true, 150.0);
    }
}

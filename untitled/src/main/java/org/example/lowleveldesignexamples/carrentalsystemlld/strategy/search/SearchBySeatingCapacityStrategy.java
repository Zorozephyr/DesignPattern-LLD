package org.example.lowleveldesignexamples.carrentalsystemlld.strategy.search;

import org.example.lowleveldesignexamples.carrentalsystemlld.model.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy for searching vehicles by seating capacity.
 * Filters vehicles that can accommodate at least the specified number of passengers.
 */
public class SearchBySeatingCapacityStrategy implements VehicleSearchStrategy {
    
    @Override
    public List<Vehicle> searchVehicles(List<Vehicle> vehicles, Object criteria) {
        if (!(criteria instanceof Integer)) {
            throw new IllegalArgumentException("Criteria must be of type Integer (minimum seating capacity)");
        }
        
        int minSeatingCapacity = (Integer) criteria;
        
        if (minSeatingCapacity <= 0) {
            throw new IllegalArgumentException("Minimum seating capacity must be positive");
        }
        
        return vehicles.stream()
                .filter(Vehicle::isAvailableForRental)
                .filter(vehicle -> vehicle.getSeatingCapacity() >= minSeatingCapacity)
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "Search by Seating Capacity";
    }
}

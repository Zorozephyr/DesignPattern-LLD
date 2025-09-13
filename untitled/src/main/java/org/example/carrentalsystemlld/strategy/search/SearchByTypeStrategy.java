package org.example.carrentalsystemlld.strategy.search;

import org.example.carrentalsystemlld.enums.VehicleType;
import org.example.carrentalsystemlld.model.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy for searching vehicles by type.
 * Filters vehicles based on VehicleType enum.
 */
public class SearchByTypeStrategy implements VehicleSearchStrategy {
    
    @Override
    public List<Vehicle> searchVehicles(List<Vehicle> vehicles, Object criteria) {
        if (!(criteria instanceof VehicleType)) {
            throw new IllegalArgumentException("Criteria must be of type VehicleType");
        }
        
        VehicleType targetType = (VehicleType) criteria;
        
        return vehicles.stream()
                .filter(Vehicle::isAvailableForRental)
                .filter(vehicle -> vehicle.getVehicleType() == targetType)
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "Search by Vehicle Type";
    }
}

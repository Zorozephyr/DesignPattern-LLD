package org.example.lowleveldesignexamples.carrentalsystemlld.strategy.search;

import org.example.lowleveldesignexamples.carrentalsystemlld.model.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy for searching vehicles by model name.
 * Filters vehicles based on partial or exact model name matching.
 */
public class SearchByModelStrategy implements VehicleSearchStrategy {
    
    private final boolean exactMatch;
    
    public SearchByModelStrategy(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }
    
    public SearchByModelStrategy() {
        this(false); // Default to partial matching
    }
    
    @Override
    public List<Vehicle> searchVehicles(List<Vehicle> vehicles, Object criteria) {
        if (!(criteria instanceof String)) {
            throw new IllegalArgumentException("Criteria must be of type String (model name)");
        }
        
        String targetModel = ((String) criteria).trim();
        
        if (targetModel.isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be empty");
        }
        
        return vehicles.stream()
                .filter(Vehicle::isAvailableForRental)
                .filter(vehicle -> matchesModel(vehicle.getModel(), targetModel))
                .collect(Collectors.toList());
    }
    
    private boolean matchesModel(String vehicleModel, String targetModel) {
        if (exactMatch) {
            return vehicleModel.equalsIgnoreCase(targetModel);
        } else {
            return vehicleModel.toLowerCase().contains(targetModel.toLowerCase());
        }
    }
    
    @Override
    public String getStrategyName() {
        return exactMatch ? "Search by Exact Model" : "Search by Model (Partial Match)";
    }
}

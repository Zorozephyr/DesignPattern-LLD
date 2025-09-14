package org.example.lowleveldesignexamples.carrentalsystemlld.strategy.search;

import org.example.lowleveldesignexamples.carrentalsystemlld.model.Vehicle;

import java.util.List;

/**
 * Strategy interface for vehicle search algorithms.
 * 
 * Design Pattern: Strategy Pattern
 * Purpose: Allows different search algorithms to be used interchangeably
 * and enables easy addition of new search criteria without modifying existing code.
 */
public interface VehicleSearchStrategy {
    
    /**
     * Searches vehicles based on the specific strategy implementation.
     * 
     * @param vehicles List of vehicles to search from
     * @param criteria Search criteria (implementation-specific)
     * @return List of vehicles matching the search criteria
     */
    List<Vehicle> searchVehicles(List<Vehicle> vehicles, Object criteria);
    
    /**
     * Gets the name of this search strategy for identification.
     * 
     * @return Strategy name
     */
    String getStrategyName();
}

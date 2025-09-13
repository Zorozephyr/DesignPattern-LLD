package org.example.carrentalsystemlld.strategy.search;

import org.example.carrentalsystemlld.model.Vehicle;

import java.util.List;

/**
 * Context class for vehicle search strategies.
 * Provides a unified interface to execute different search strategies.
 * 
 * Design Pattern: Strategy Pattern
 * Purpose: Allows runtime selection of search algorithms and decouples
 * search logic from the client code.
 */
public class VehicleSearchContext {
    
    private VehicleSearchStrategy searchStrategy;
    
    public VehicleSearchContext(VehicleSearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }
    
    /**
     * Sets a new search strategy.
     * 
     * @param searchStrategy The new search strategy to use
     */
    public void setSearchStrategy(VehicleSearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }
    
    /**
     * Executes the current search strategy.
     * 
     * @param vehicles List of vehicles to search from
     * @param criteria Search criteria (strategy-specific)
     * @return List of vehicles matching the search criteria
     */
    public List<Vehicle> executeSearch(List<Vehicle> vehicles, Object criteria) {
        if (searchStrategy == null) {
            throw new IllegalStateException("Search strategy not set");
        }
        
        return searchStrategy.searchVehicles(vehicles, criteria);
    }
    
    /**
     * Gets the name of the current search strategy.
     * 
     * @return Strategy name
     */
    public String getCurrentStrategyName() {
        return searchStrategy != null ? searchStrategy.getStrategyName() : "No strategy set";
    }
}

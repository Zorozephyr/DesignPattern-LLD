package org.example.carrentalsystemlld.strategy.search;

import org.example.carrentalsystemlld.model.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy for searching vehicles by price range.
 * Filters vehicles based on daily rental cost within specified range.
 */
public class SearchByPriceRangeStrategy implements VehicleSearchStrategy {
    
    /**
     * Inner class to represent price range criteria.
     */
    public static class PriceRange {
        private final double minPrice;
        private final double maxPrice;
        
        public PriceRange(double minPrice, double maxPrice) {
            if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
                throw new IllegalArgumentException("Invalid price range");
            }
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }
        
        public double getMinPrice() { return minPrice; }
        public double getMaxPrice() { return maxPrice; }
        
        public boolean isInRange(double price) {
            return price >= minPrice && price <= maxPrice;
        }
    }
    
    @Override
    public List<Vehicle> searchVehicles(List<Vehicle> vehicles, Object criteria) {
        if (!(criteria instanceof PriceRange)) {
            throw new IllegalArgumentException("Criteria must be of type PriceRange");
        }
        
        PriceRange priceRange = (PriceRange) criteria;
        
        return vehicles.stream()
                .filter(Vehicle::isAvailableForRental)
                .filter(vehicle -> priceRange.isInRange(vehicle.getDailyRentalCost()))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "Search by Price Range";
    }
}

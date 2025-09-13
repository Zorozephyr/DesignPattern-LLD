package org.example.carrentalsystemlld.repository;

import org.example.carrentalsystemlld.enums.VehicleStatus;
import org.example.carrentalsystemlld.enums.VehicleType;
import org.example.carrentalsystemlld.exceptions.VehicleNotAvailableException;
import org.example.carrentalsystemlld.model.Vehicle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Repository class for managing vehicle inventory.
 * Implements Repository Pattern to abstract data access layer.
 * 
 * Design Pattern: Repository Pattern
 * Purpose: Provides a clean separation between business logic and data access,
 * making the system more testable and maintainable.
 */
public class VehicleInventoryManagement {
    
    private static final Logger logger = Logger.getLogger(VehicleInventoryManagement.class.getName());
    
    // Using ConcurrentHashMap for thread-safe operations
    private final Map<String, Vehicle> vehicles;
    private final String storeId;
    
    public VehicleInventoryManagement(String storeId) {
        this.storeId = storeId;
        this.vehicles = new ConcurrentHashMap<>();
        logger.info("Initialized vehicle inventory for store: " + storeId);
    }
    
    /**
     * Adds a new vehicle to the inventory.
     * 
     * @param vehicle Vehicle to add
     * @throws IllegalArgumentException if vehicle already exists
     */
    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        
        if (vehicles.containsKey(vehicle.getVehicleId())) {
            throw new IllegalArgumentException("Vehicle with ID " + vehicle.getVehicleId() + " already exists");
        }
        
        vehicles.put(vehicle.getVehicleId(), vehicle);
        logger.info("Added vehicle to inventory: " + vehicle.getVehicleId() + " - " + vehicle.getModel());
    }
    
    /**
     * Removes a vehicle from the inventory.
     * 
     * @param vehicleId ID of the vehicle to remove
     * @return true if vehicle was removed, false if not found
     */
    public boolean removeVehicle(String vehicleId) {
        Vehicle removedVehicle = vehicles.remove(vehicleId);
        if (removedVehicle != null) {
            logger.info("Removed vehicle from inventory: " + vehicleId);
            return true;
        }
        logger.warning("Attempted to remove non-existent vehicle: " + vehicleId);
        return false;
    }
    
    /**
     * Updates an existing vehicle in the inventory.
     * 
     * @param vehicle Updated vehicle information
     * @throws IllegalArgumentException if vehicle doesn't exist
     */
    public void updateVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        
        if (!vehicles.containsKey(vehicle.getVehicleId())) {
            throw new IllegalArgumentException("Vehicle with ID " + vehicle.getVehicleId() + " not found");
        }
        
        vehicles.put(vehicle.getVehicleId(), vehicle);
        logger.info("Updated vehicle in inventory: " + vehicle.getVehicleId());
    }
    
    /**
     * Retrieves a vehicle by its ID.
     * 
     * @param vehicleId ID of the vehicle to retrieve
     * @return Vehicle if found, null otherwise
     */
    public Vehicle getVehicleById(String vehicleId) {
        return vehicles.get(vehicleId);
    }
    
    /**
     * Gets all vehicles in the inventory.
     * 
     * @return List of all vehicles
     */
    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles.values());
    }
    
    /**
     * Gets all available vehicles for rental.
     * 
     * @return List of available vehicles
     */
    public List<Vehicle> getAvailableVehicles() {
        return vehicles.values().stream()
                .filter(Vehicle::isAvailableForRental)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets vehicles by type.
     * 
     * @param vehicleType Type of vehicles to retrieve
     * @return List of vehicles of the specified type
     */
    public List<Vehicle> getVehiclesByType(VehicleType vehicleType) {
        return vehicles.values().stream()
                .filter(vehicle -> vehicle.getVehicleType() == vehicleType)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets available vehicles by type.
     * 
     * @param vehicleType Type of vehicles to retrieve
     * @return List of available vehicles of the specified type
     */
    public List<Vehicle> getAvailableVehiclesByType(VehicleType vehicleType) {
        return vehicles.values().stream()
                .filter(Vehicle::isAvailableForRental)
                .filter(vehicle -> vehicle.getVehicleType() == vehicleType)
                .collect(Collectors.toList());
    }
    
    /**
     * Reserves a vehicle (changes status to RESERVED).
     * 
     * @param vehicleId ID of the vehicle to reserve
     * @throws VehicleNotAvailableException if vehicle is not available
     */
    public void reserveVehicle(String vehicleId) throws VehicleNotAvailableException {
        Vehicle vehicle = vehicles.get(vehicleId);
        
        if (vehicle == null) {
            throw new VehicleNotAvailableException("Vehicle not found: " + vehicleId);
        }
        
        if (!vehicle.isAvailableForRental()) {
            throw new VehicleNotAvailableException(vehicleId);
        }
        
        vehicle.setStatus(VehicleStatus.RESERVED);
        logger.info("Reserved vehicle: " + vehicleId);
    }
    
    /**
     * Releases a vehicle (changes status back to AVAILABLE).
     * 
     * @param vehicleId ID of the vehicle to release
     */
    public void releaseVehicle(String vehicleId) {
        Vehicle vehicle = vehicles.get(vehicleId);
        
        if (vehicle != null && vehicle.getStatus() == VehicleStatus.RESERVED) {
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            logger.info("Released vehicle: " + vehicleId);
        }
    }
    
    /**
     * Sets a vehicle to maintenance status.
     * 
     * @param vehicleId ID of the vehicle to set to maintenance
     */
    public void setVehicleToMaintenance(String vehicleId) {
        Vehicle vehicle = vehicles.get(vehicleId);
        
        if (vehicle != null) {
            vehicle.setStatus(VehicleStatus.MAINTENANCE);
            logger.info("Set vehicle to maintenance: " + vehicleId);
        }
    }
    
    /**
     * Gets inventory statistics.
     * 
     * @return Map containing inventory statistics
     */
    public Map<String, Integer> getInventoryStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("Total Vehicles", vehicles.size());
        stats.put("Available", (int) vehicles.values().stream()
                .filter(Vehicle::isAvailableForRental).count());
        stats.put("Reserved", (int) vehicles.values().stream()
                .filter(v -> v.getStatus() == VehicleStatus.RESERVED).count());
        stats.put("In Maintenance", (int) vehicles.values().stream()
                .filter(v -> v.getStatus() == VehicleStatus.MAINTENANCE).count());
        stats.put("Retired", (int) vehicles.values().stream()
                .filter(v -> v.getStatus() == VehicleStatus.RETIRED).count());
        
        // Statistics by vehicle type
        for (VehicleType type : VehicleType.values()) {
            stats.put(type.getDisplayName() + "s", (int) vehicles.values().stream()
                    .filter(v -> v.getVehicleType() == type).count());
        }
        
        return stats;
    }
    
    /**
     * Checks if a vehicle exists in the inventory.
     * 
     * @param vehicleId ID of the vehicle to check
     * @return true if vehicle exists, false otherwise
     */
    public boolean vehicleExists(String vehicleId) {
        return vehicles.containsKey(vehicleId);
    }
    
    /**
     * Gets the total count of vehicles in the inventory.
     * 
     * @return Total vehicle count
     */
    public int getTotalVehicleCount() {
        return vehicles.size();
    }
    
    /**
     * Gets the count of available vehicles.
     * 
     * @return Available vehicle count
     */
    public int getAvailableVehicleCount() {
        return (int) vehicles.values().stream()
                .filter(Vehicle::isAvailableForRental)
                .count();
    }
    
    public String getStoreId() {
        return storeId;
    }
}

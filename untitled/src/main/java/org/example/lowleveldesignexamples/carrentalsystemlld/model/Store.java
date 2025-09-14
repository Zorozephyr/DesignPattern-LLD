package org.example.lowleveldesignexamples.carrentalsystemlld.model;

import org.example.lowleveldesignexamples.carrentalsystemlld.repository.VehicleInventoryManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a rental store/location in the car rental system.
 * Contains inventory management and reservation tracking.
 */
public class Store {
    private String storeId;
    private String storeName;
    private Location location;
    private VehicleInventoryManagement inventoryManagement;
    private List<Reservation> reservations;
    private String contactPhone;
    private String managerName;
    private boolean isOperational;

    public Store(String storeId, String storeName, Location location, 
                String contactPhone, String managerName) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.location = location;
        this.contactPhone = contactPhone;
        this.managerName = managerName;
        this.inventoryManagement = new VehicleInventoryManagement(storeId);
        this.reservations = new ArrayList<>();
        this.isOperational = true;
    }

    /**
     * Adds a reservation to this store.
     * 
     * @param reservation Reservation to add
     */
    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            reservations.add(reservation);
        }
    }

    /**
     * Removes a reservation from this store.
     * 
     * @param reservationId ID of the reservation to remove
     * @return true if reservation was removed, false if not found
     */
    public boolean removeReservation(String reservationId) {
        return reservations.removeIf(reservation -> 
                reservation.getReservationId().equals(reservationId));
    }

    /**
     * Gets a reservation by ID.
     * 
     * @param reservationId ID of the reservation to find
     * @return Reservation if found, null otherwise
     */
    public Reservation getReservationById(String reservationId) {
        return reservations.stream()
                .filter(reservation -> reservation.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all active reservations for this store.
     * 
     * @return List of active reservations
     */
    public List<Reservation> getActiveReservations() {
        return reservations.stream()
                .filter(reservation -> reservation.getStatus().isActive())
                .collect(Collectors.toList());
    }

    /**
     * Gets all reservations for a specific user.
     * 
     * @param userId ID of the user
     * @return List of reservations for the user
     */
    public List<Reservation> getReservationsForUser(String userId) {
        return reservations.stream()
                .filter(reservation -> reservation.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Gets store operational summary.
     * 
     * @return String containing store summary
     */
    public String getStoreSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Store: ").append(storeName).append(" (").append(storeId).append(")\n");
        summary.append("Location: ").append(location.getCity()).append(", ").append(location.getState()).append("\n");
        summary.append("Manager: ").append(managerName).append("\n");
        summary.append("Contact: ").append(contactPhone).append("\n");
        summary.append("Status: ").append(isOperational ? "Operational" : "Closed").append("\n");
        summary.append("Total Vehicles: ").append(inventoryManagement.getTotalVehicleCount()).append("\n");
        summary.append("Available Vehicles: ").append(inventoryManagement.getAvailableVehicleCount()).append("\n");
        summary.append("Active Reservations: ").append(getActiveReservations().size()).append("\n");
        
        return summary.toString();
    }

    /**
     * Closes the store temporarily.
     */
    public void closeStore() {
        this.isOperational = false;
    }

    /**
     * Opens the store for operations.
     */
    public void openStore() {
        this.isOperational = true;
    }

    // Getters and Setters
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public VehicleInventoryManagement getInventoryManagement() {
        return inventoryManagement;
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setOperational(boolean operational) {
        isOperational = operational;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(storeId, store.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId);
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", location=" + location.getCity() +
                ", isOperational=" + isOperational +
                '}';
    }
}

package org.example.carrentalsystemlld;

import org.example.carrentalsystemlld.enums.ReservationStatus;
import org.example.carrentalsystemlld.enums.VehicleStatus;
import org.example.carrentalsystemlld.exceptions.CarRentalException;
import org.example.carrentalsystemlld.exceptions.InvalidReservationException;
import org.example.carrentalsystemlld.exceptions.VehicleNotAvailableException;
import org.example.carrentalsystemlld.model.*;
import org.example.carrentalsystemlld.strategy.search.VehicleSearchContext;
import org.example.carrentalsystemlld.strategy.payment.PaymentContext;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Main system class for the car rental application.
 * Implements Singleton Pattern to ensure only one instance exists.
 * 
 * Design Pattern: Singleton Pattern
 * Purpose: Ensures single point of control for the entire rental system,
 * maintains global state consistency, and provides centralized access.
 */
public class VehicleRentalSystem {
    
    private static final Logger logger = Logger.getLogger(VehicleRentalSystem.class.getName());
    
    // Singleton instance
    private static volatile VehicleRentalSystem instance;
    
    // System data storage
    private final Map<String, User> users;
    private final Map<String, Store> stores;
    private final Map<String, Location> locations;
    private final Map<String, Reservation> reservations;
    private final Map<String, Bill> bills;
    private final Map<String, Payment> payments;
    
    // System configuration
    private final String systemName;
    private final String version;
    
    /**
     * Private constructor to prevent direct instantiation.
     */
    private VehicleRentalSystem() {
        this.users = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.locations = new ConcurrentHashMap<>();
        this.reservations = new ConcurrentHashMap<>();
        this.bills = new ConcurrentHashMap<>();
        this.payments = new ConcurrentHashMap<>();
        this.systemName = "CarRental Pro";
        this.version = "1.0.0";
        
        logger.info("Vehicle Rental System initialized - " + systemName + " v" + version);
    }
    
    /**
     * Gets the singleton instance of the VehicleRentalSystem.
     * Thread-safe implementation using double-checked locking.
     * 
     * @return The singleton instance
     */
    public static VehicleRentalSystem getInstance() {
        if (instance == null) {
            synchronized (VehicleRentalSystem.class) {
                if (instance == null) {
                    instance = new VehicleRentalSystem();
                }
            }
        }
        return instance;
    }
    
    // ================== USER MANAGEMENT ==================
    
    /**
     * Registers a new user in the system.
     * 
     * @param user User to register
     * @throws IllegalArgumentException if user already exists
     */
    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User with ID " + user.getUserId() + " already exists");
        }
        
        users.put(user.getUserId(), user);
        logger.info("Registered new user: " + user.getUserId() + " - " + user.getName());
    }
    
    /**
     * Gets a user by ID.
     * 
     * @param userId User ID
     * @return User if found, null otherwise
     */
    public User getUserById(String userId) {
        return users.get(userId);
    }
    
    /**
     * Gets all registered users.
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // ================== STORE MANAGEMENT ==================
    
    /**
     * Adds a new store to the system.
     * 
     * @param store Store to add
     * @throws IllegalArgumentException if store already exists
     */
    public void addStore(Store store) {
        if (store == null) {
            throw new IllegalArgumentException("Store cannot be null");
        }
        
        if (stores.containsKey(store.getStoreId())) {
            throw new IllegalArgumentException("Store with ID " + store.getStoreId() + " already exists");
        }
        
        stores.put(store.getStoreId(), store);
        
        // Also add the store's location to the locations map
        if (store.getLocation() != null) {
            locations.put(store.getLocation().getLocationId(), store.getLocation());
        }
        
        logger.info("Added new store: " + store.getStoreId() + " - " + store.getStoreName());
    }
    
    /**
     * Gets a store by ID.
     * 
     * @param storeId Store ID
     * @return Store if found, null otherwise
     */
    public Store getStoreById(String storeId) {
        return stores.get(storeId);
    }
    
    /**
     * Gets all stores in the system.
     * 
     * @return List of all stores
     */
    public List<Store> getAllStores() {
        return new ArrayList<>(stores.values());
    }
    
    /**
     * Gets operational stores only.
     * 
     * @return List of operational stores
     */
    public List<Store> getOperationalStores() {
        return stores.values().stream()
                .filter(Store::isOperational)
                .collect(Collectors.toList());
    }
    
    // ================== LOCATION MANAGEMENT ==================
    
    /**
     * Adds a location to the system.
     * 
     * @param location Location to add
     */
    public void addLocation(Location location) {
        if (location != null) {
            locations.put(location.getLocationId(), location);
        }
    }
    
    /**
     * Gets a location by ID.
     * 
     * @param locationId Location ID
     * @return Location if found, null otherwise
     */
    public Location getLocationById(String locationId) {
        return locations.get(locationId);
    }
    
    // ================== VEHICLE SEARCH ==================
    
    /**
     * Searches for vehicles using the provided search context.
     * 
     * @param searchContext Configured search context with strategy
     * @param criteria Search criteria
     * @return List of vehicles matching the search criteria
     */
    public List<Vehicle> searchVehicles(VehicleSearchContext searchContext, Object criteria) {
        List<Vehicle> allVehicles = new ArrayList<>();
        
        // Collect vehicles from all operational stores
        for (Store store : getOperationalStores()) {
            allVehicles.addAll(store.getInventoryManagement().getAllVehicles());
        }
        
        return searchContext.executeSearch(allVehicles, criteria);
    }
    
    /**
     * Gets all available vehicles across all stores.
     * 
     * @return List of available vehicles
     */
    public List<Vehicle> getAllAvailableVehicles() {
        List<Vehicle> availableVehicles = new ArrayList<>();
        
        for (Store store : getOperationalStores()) {
            availableVehicles.addAll(store.getInventoryManagement().getAvailableVehicles());
        }
        
        return availableVehicles;
    }
    
    // ================== RESERVATION MANAGEMENT ==================
    
    /**
     * Creates a new reservation.
     * 
     * @param userId User ID making the reservation
     * @param vehicleId Vehicle ID to reserve
     * @param startDate Rental start date
     * @param endDate Rental end date
     * @param pickupLocationId Pickup location ID
     * @param dropLocationId Drop location ID
     * @return Created reservation
     * @throws CarRentalException if reservation cannot be created
     */
    public Reservation createReservation(String userId, String vehicleId, 
                                       LocalDate startDate, LocalDate endDate,
                                       String pickupLocationId, String dropLocationId) 
                                       throws CarRentalException {
        
        // Validate inputs
        User user = getUserById(userId);
        if (user == null) {
            throw new InvalidReservationException("User not found: " + userId);
        }
        
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle == null) {
            throw new VehicleNotAvailableException("Vehicle not found: " + vehicleId);
        }
        
        if (!vehicle.isAvailableForRental()) {
            throw new VehicleNotAvailableException(vehicleId);
        }
        
        Location pickupLocation = getLocationById(pickupLocationId);
        Location dropLocation = getLocationById(dropLocationId);
        
        if (pickupLocation == null || dropLocation == null) {
            throw new InvalidReservationException("Invalid pickup or drop location");
        }
        
        if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
            throw new InvalidReservationException("Invalid reservation dates");
        }
        
        // Create reservation
        String reservationId = generateReservationId();
        Reservation reservation = new Reservation(reservationId, user, vehicle, 
                                                startDate, endDate, pickupLocation, dropLocation);
        
        // Reserve the vehicle
        Store vehicleStore = findStoreByVehicle(vehicleId);
        if (vehicleStore != null) {
            vehicleStore.getInventoryManagement().reserveVehicle(vehicleId);
            vehicleStore.addReservation(reservation);
        }
        
        // Add to user's rental history
        user.addReservation(reservation);
        
        // Store reservation
        reservations.put(reservationId, reservation);
        
        logger.info("Created reservation: " + reservationId + " for user " + userId);
        return reservation;
    }
    
    /**
     * Cancels a reservation.
     * 
     * @param reservationId Reservation ID to cancel
     * @return true if reservation was cancelled, false otherwise
     */
    public boolean cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        
        if (reservation == null) {
            logger.warning("Attempted to cancel non-existent reservation: " + reservationId);
            return false;
        }
        
        if (reservation.getStatus() != ReservationStatus.SCHEDULED) {
            logger.warning("Cannot cancel reservation in status: " + reservation.getStatus());
            return false;
        }
        
        // Cancel the reservation
        reservation.cancelReservation();
        
        // Release the vehicle
        Store store = findStoreByVehicle(reservation.getVehicle().getVehicleId());
        if (store != null) {
            store.getInventoryManagement().releaseVehicle(reservation.getVehicle().getVehicleId());
        }
        
        logger.info("Cancelled reservation: " + reservationId);
        return true;
    }
    
    /**
     * Gets a reservation by ID.
     * 
     * @param reservationId Reservation ID
     * @return Reservation if found, null otherwise
     */
    public Reservation getReservationById(String reservationId) {
        return reservations.get(reservationId);
    }
    
    // ================== BILLING AND PAYMENT ==================
    
    /**
     * Generates a bill for a reservation.
     * 
     * @param reservationId Reservation ID
     * @return Generated bill
     * @throws InvalidReservationException if reservation not found
     */
    public Bill generateBill(String reservationId) throws InvalidReservationException {
        Reservation reservation = reservations.get(reservationId);
        
        if (reservation == null) {
            throw new InvalidReservationException("Reservation not found: " + reservationId);
        }
        
        String billId = generateBillId();
        Bill bill = new Bill(billId, reservation);
        
        bills.put(billId, bill);
        
        logger.info("Generated bill: " + billId + " for reservation " + reservationId);
        return bill;
    }
    
    /**
     * Processes a payment using the provided payment context.
     * 
     * @param paymentContext Configured payment context with strategy
     * @param billId Bill ID to pay
     * @param paymentDetails Payment details (strategy-specific)
     * @return Created payment
     * @throws CarRentalException if payment cannot be processed
     */
    public Payment processPayment(PaymentContext paymentContext, String billId, 
                                Object paymentDetails) throws CarRentalException {
        
        Bill bill = bills.get(billId);
        if (bill == null) {
            throw new InvalidReservationException("Bill not found: " + billId);
        }
        
        if (bill.isPaid()) {
            throw new InvalidReservationException("Bill already paid: " + billId);
        }
        
        String paymentId = generatePaymentId();
        Payment payment = new Payment(paymentId, bill, bill.getTotalAmount(), 
                                    paymentContext.getCurrentPaymentMethod());
        
        boolean paymentSuccessful = paymentContext.processPayment(payment, paymentDetails);
        
        payments.put(paymentId, payment);
        
        if (paymentSuccessful) {
            logger.info("Payment processed successfully: " + paymentId);
        } else {
            logger.warning("Payment failed: " + paymentId);
        }
        
        return payment;
    }
    
    // ================== SYSTEM UTILITIES ==================
    
    /**
     * Finds a vehicle by ID across all stores.
     * 
     * @param vehicleId Vehicle ID to find
     * @return Vehicle if found, null otherwise
     */
    private Vehicle findVehicleById(String vehicleId) {
        for (Store store : stores.values()) {
            Vehicle vehicle = store.getInventoryManagement().getVehicleById(vehicleId);
            if (vehicle != null) {
                return vehicle;
            }
        }
        return null;
    }
    
    /**
     * Finds the store that contains a specific vehicle.
     * 
     * @param vehicleId Vehicle ID to search for
     * @return Store containing the vehicle, null if not found
     */
    private Store findStoreByVehicle(String vehicleId) {
        for (Store store : stores.values()) {
            if (store.getInventoryManagement().vehicleExists(vehicleId)) {
                return store;
            }
        }
        return null;
    }
    
    /**
     * Gets comprehensive system statistics.
     * 
     * @return Map containing system statistics
     */
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("Total Users", users.size());
        stats.put("Total Stores", stores.size());
        stats.put("Operational Stores", getOperationalStores().size());
        stats.put("Total Locations", locations.size());
        stats.put("Total Reservations", reservations.size());
        stats.put("Active Reservations", reservations.values().stream()
                .filter(r -> r.getStatus().isActive()).count());
        stats.put("Total Bills", bills.size());
        stats.put("Paid Bills", bills.values().stream().filter(Bill::isPaid).count());
        stats.put("Total Payments", payments.size());
        
        // Vehicle statistics across all stores
        int totalVehicles = 0;
        int availableVehicles = 0;
        for (Store store : stores.values()) {
            totalVehicles += store.getInventoryManagement().getTotalVehicleCount();
            availableVehicles += store.getInventoryManagement().getAvailableVehicleCount();
        }
        
        stats.put("Total Vehicles", totalVehicles);
        stats.put("Available Vehicles", availableVehicles);
        
        return stats;
    }
    
    // ID generation methods
    private String generateReservationId() {
        return "RES_" + System.currentTimeMillis() + "_" + 
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private String generateBillId() {
        return "BILL_" + System.currentTimeMillis() + "_" + 
               UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
    
    private String generatePaymentId() {
        return "PAY_" + System.currentTimeMillis() + "_" + 
               UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
    
    // Getters
    public String getSystemName() {
        return systemName;
    }
    
    public String getVersion() {
        return version;
    }
}

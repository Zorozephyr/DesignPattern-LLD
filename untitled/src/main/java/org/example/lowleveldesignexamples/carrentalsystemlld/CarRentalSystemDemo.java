package org.example.lowleveldesignexamples.carrentalsystemlld;

import org.example.lowleveldesignexamples.carrentalsystemlld.enums.VehicleType;
import org.example.lowleveldesignexamples.carrentalsystemlld.exceptions.CarRentalException;
import org.example.lowleveldesignexamples.carrentalsystemlld.factory.VehicleFactory;
import org.example.carrentalsystemlld.model.*;
import org.example.carrentalsystemlld.strategy.payment.*;
import org.example.carrentalsystemlld.strategy.search.*;
import org.example.lowleveldesignexamples.carrentalsystemlld.model.*;
import org.example.lowleveldesignexamples.carrentalsystemlld.strategy.payment.CashPaymentStrategy;
import org.example.lowleveldesignexamples.carrentalsystemlld.strategy.payment.CreditCardPaymentStrategy;
import org.example.lowleveldesignexamples.carrentalsystemlld.strategy.payment.PayPalPaymentStrategy;
import org.example.lowleveldesignexamples.carrentalsystemlld.strategy.payment.PaymentContext;
import org.example.lowleveldesignexamples.carrentalsystemlld.strategy.search.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Comprehensive demonstration of the Car Rental System.
 * Shows all design patterns in action and system capabilities.
 * 
 * Design Patterns Demonstrated:
 * 1. Singleton Pattern - VehicleRentalSystem
 * 2. Factory Pattern - VehicleFactory
 * 3. Strategy Pattern - Search and Payment strategies
 * 4. Repository Pattern - VehicleInventoryManagement
 */
public class CarRentalSystemDemo {
    
    private static final Logger logger = Logger.getLogger(CarRentalSystemDemo.class.getName());
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("       CAR RENTAL SYSTEM DEMONSTRATION");
        System.out.println("=".repeat(60));
        
        try {
            // Initialize the system
            CarRentalSystemDemo demo = new CarRentalSystemDemo();
            demo.runCompleteDemo();
            
        } catch (Exception e) {
            logger.severe("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void runCompleteDemo() throws CarRentalException {
        // Get the singleton instance
        VehicleRentalSystem rentalSystem = VehicleRentalSystem.getInstance();
        
        System.out.println("\\n🚗 " + rentalSystem.getSystemName() + " v" + rentalSystem.getVersion());
        System.out.println("Demonstrating Design Patterns in Car Rental System\\n");
        
        // Step 1: Setup system data
        setupSystemData(rentalSystem);
        
        // Step 2: Demonstrate Factory Pattern
        demonstrateFactoryPattern(rentalSystem);
        
        // Step 3: Demonstrate Strategy Pattern - Search
        demonstrateSearchStrategies(rentalSystem);
        
        // Step 4: Demonstrate Repository Pattern
        demonstrateRepositoryPattern(rentalSystem);
        
        // Step 5: Demonstrate Reservation Process
        demonstrateReservationProcess(rentalSystem);
        
        // Step 6: Demonstrate Strategy Pattern - Payment
        demonstratePaymentStrategies(rentalSystem);
        
        // Step 7: Show System Statistics
        showSystemStatistics(rentalSystem);
        
        System.out.println("\\n" + "=".repeat(60));
        System.out.println("       DEMONSTRATION COMPLETED SUCCESSFULLY");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Sets up initial system data including users, locations, stores, and vehicles.
     */
    private void setupSystemData(VehicleRentalSystem rentalSystem) {
        System.out.println("📋 STEP 1: Setting up system data...");
        
        // Create users
        User user1 = new User("U001", "John Doe", "john.doe@email.com",
                             "+1-555-0123", "DL12345", "123 Main St, Anytown, USA");
        User user2 = new User("U002", "Jane Smith", "jane.smith@email.com", 
                             "+1-555-0456", "DL67890", "456 Oak Ave, Somewhere, USA");
        
        rentalSystem.registerUser(user1);
        rentalSystem.registerUser(user2);
        
        // Create locations
        Location location1 = new Location("LOC001", "123 Business Blvd", "New York",
                                         "NY", "10001", "USA", 40.7128, -74.0060);
        Location location2 = new Location("LOC002", "456 Commerce St", "Los Angeles", 
                                         "CA", "90210", "USA", 34.0522, -118.2437);
        Location location3 = new Location("LOC003", "789 Trade Center", "Chicago", 
                                         "IL", "60601", "USA", 41.8781, -87.6298);
        
        // Create stores
        Store store1 = new Store("STORE001", "CarRental Pro NYC", location1, 
                               "+1-555-NYC1", "Mike Johnson");
        Store store2 = new Store("STORE002", "CarRental Pro LA", location2, 
                               "+1-555-LA01", "Sarah Wilson");
        Store store3 = new Store("STORE003", "CarRental Pro Chicago", location3, 
                               "+1-555-CHI1", "David Brown");
        
        rentalSystem.addStore(store1);
        rentalSystem.addStore(store2);
        rentalSystem.addStore(store3);
        
        System.out.println("✅ System data setup completed:");
        System.out.println("   • Users: " + rentalSystem.getAllUsers().size());
        System.out.println("   • Stores: " + rentalSystem.getAllStores().size());
    }
    
    /**
     * Demonstrates the Factory Pattern by creating different types of vehicles.
     */
    private void demonstrateFactoryPattern(VehicleRentalSystem rentalSystem) {
        System.out.println("\\n🏭 STEP 2: Demonstrating Factory Pattern...");
        System.out.println("Creating vehicles using VehicleFactory:");
        
        Store nyStore = rentalSystem.getStoreById("STORE001");
        Store laStore = rentalSystem.getStoreById("STORE002");
        Store chiStore = rentalSystem.getStoreById("STORE003");
        
        // Create vehicles using Factory Pattern - default configurations
        Vehicle car1 = VehicleFactory.createVehicle(VehicleType.CAR, "V001", "ABC123",
                                                   50.0, "Camry", "Toyota", 2022);
        Vehicle car2 = VehicleFactory.createVehicle(VehicleType.CAR, "V002", "DEF456", 
                                                   45.0, "Civic", "Honda", 2023);
        
        // Create vehicles with custom configurations
        Vehicle luxuryCar = VehicleFactory.createCar("V003", "GHI789", 120.0, "Model S", 
                                                    "Tesla", 2023, 5, "Electric", true, true, "Automatic", 4);
        
        Vehicle motorcycle = VehicleFactory.createMotorcycle("V004", "JKL012", 25.0, 
                                                            "Ninja", "Kawasaki", 2022, "Petrol", 650, true, "Sports");
        
        Vehicle truck = VehicleFactory.createTruck("V005", "MNO345", 80.0, "F-150", 
                                                  "Ford", 2021, 3, "Diesel", 3.5, true, "Pickup", false);
        
        Vehicle van = VehicleFactory.createVan("V006", "PQR678", 70.0, "Transit", 
                                              "Ford", 2022, 12, "Diesel", true, true, true, 200.0);
        
        // Add vehicles to stores
        nyStore.getInventoryManagement().addVehicle(car1);
        nyStore.getInventoryManagement().addVehicle(luxuryCar);
        laStore.getInventoryManagement().addVehicle(car2);
        laStore.getInventoryManagement().addVehicle(motorcycle);
        chiStore.getInventoryManagement().addVehicle(truck);
        chiStore.getInventoryManagement().addVehicle(van);
        
        System.out.println("✅ Factory Pattern demonstrated:");
        System.out.println("   • Created " + VehicleType.values().length + " different vehicle types");
        System.out.println("   • " + car1.getVehicleFeatures());
        System.out.println("   • " + motorcycle.getVehicleFeatures());
        System.out.println("   • " + truck.getVehicleFeatures());
    }
    
    /**
     * Demonstrates the Strategy Pattern for vehicle search.
     */
    private void demonstrateSearchStrategies(VehicleRentalSystem rentalSystem) {
        System.out.println("\\n🔍 STEP 3: Demonstrating Search Strategy Pattern...");
        
        // Strategy 1: Search by Type
        VehicleSearchContext searchContext = new VehicleSearchContext(new SearchByTypeStrategy());
        List<Vehicle> cars = rentalSystem.searchVehicles(searchContext, VehicleType.CAR);
        System.out.println("Strategy 1 - Search by Type (Cars): Found " + cars.size() + " cars");
        
        // Strategy 2: Search by Price Range
        searchContext.setSearchStrategy(new SearchByPriceRangeStrategy());
        SearchByPriceRangeStrategy.PriceRange priceRange = 
            new SearchByPriceRangeStrategy.PriceRange(40.0, 80.0);
        List<Vehicle> affordableVehicles = rentalSystem.searchVehicles(searchContext, priceRange);
        System.out.println("Strategy 2 - Search by Price Range ($40-$80): Found " + 
                          affordableVehicles.size() + " vehicles");
        
        // Strategy 3: Search by Seating Capacity
        searchContext.setSearchStrategy(new SearchBySeatingCapacityStrategy());
        List<Vehicle> familyVehicles = rentalSystem.searchVehicles(searchContext, 5);
        System.out.println("Strategy 3 - Search by Seating (5+ seats): Found " + 
                          familyVehicles.size() + " vehicles");
        
        // Strategy 4: Search by Model
        searchContext.setSearchStrategy(new SearchByModelStrategy());
        List<Vehicle> toyotaVehicles = rentalSystem.searchVehicles(searchContext, "Toyota");
        System.out.println("Strategy 4 - Search by Model (Toyota): Found " + 
                          toyotaVehicles.size() + " vehicles");
        
        System.out.println("✅ Search Strategy Pattern demonstrated with 4 different strategies");
    }
    
    /**
     * Demonstrates the Repository Pattern for inventory management.
     */
    private void demonstrateRepositoryPattern(VehicleRentalSystem rentalSystem) {
        System.out.println("\\n📦 STEP 4: Demonstrating Repository Pattern...");
        
        Store nyStore = rentalSystem.getStoreById("STORE001");
        var inventory = nyStore.getInventoryManagement();
        
        System.out.println("Repository operations on " + nyStore.getStoreName() + ":");
        
        // Show inventory statistics
        Map<String, Integer> stats = inventory.getInventoryStatistics();
        System.out.println("   • Total vehicles: " + stats.get("Total Vehicles"));
        System.out.println("   • Available vehicles: " + stats.get("Available"));
        System.out.println("   • Cars: " + stats.get("Cars"));
        
        // Demonstrate CRUD operations
        System.out.println("\\nDemonstrating CRUD operations:");
        
        // Read operation
        Vehicle vehicle = inventory.getVehicleById("V001");
        System.out.println("   • READ: Retrieved " + (vehicle != null ? vehicle.getModel() : "null"));
        
        // Update operation
        if (vehicle != null) {
            vehicle.setDailyRentalCost(55.0);
            inventory.updateVehicle(vehicle);
            System.out.println("   • UPDATE: Modified daily cost to $55.0");
        }
        
        System.out.println("✅ Repository Pattern demonstrated with CRUD operations");
    }
    
    /**
     * Demonstrates the complete reservation process.
     */
    private void demonstrateReservationProcess(VehicleRentalSystem rentalSystem) 
            throws CarRentalException {
        System.out.println("\\n📅 STEP 5: Demonstrating Reservation Process...");
        
        // Create a reservation
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(4);
        
        Reservation reservation = rentalSystem.createReservation(
            "U001", "V001", startDate, endDate, "LOC001", "LOC001"
        );
        
        System.out.println("✅ Reservation created:");
        System.out.println("   • Reservation ID: " + reservation.getReservationId());
        System.out.println("   • Vehicle: " + reservation.getVehicle().getModel());
        System.out.println("   • Duration: " + reservation.getRentalDays() + " days");
        System.out.println("   • Total Cost: $" + String.format("%.2f", reservation.getTotalCost()));
        
        // Generate bill
        Bill bill = rentalSystem.generateBill(reservation.getReservationId());
        System.out.println("\\n💰 Bill generated:");
        System.out.println("   • Bill ID: " + bill.getBillId());
        System.out.println("   • Total Amount: $" + String.format("%.2f", bill.getTotalAmount()));
        
        // Store the reservation and bill IDs for payment demonstration
        this.sampleReservationId = reservation.getReservationId();
        this.sampleBillId = bill.getBillId();
    }
    
    private String sampleReservationId;
    private String sampleBillId;
    
    /**
     * Demonstrates the Strategy Pattern for payment processing.
     */
    private void demonstratePaymentStrategies(VehicleRentalSystem rentalSystem) 
            throws CarRentalException {
        System.out.println("\\n💳 STEP 6: Demonstrating Payment Strategy Pattern...");
        
        if (sampleBillId == null) {
            System.out.println("⚠️ No bill available for payment demonstration");
            return;
        }
        
        // Strategy 1: Credit Card Payment
        PaymentContext paymentContext = new PaymentContext(new CreditCardPaymentStrategy());
        CreditCardPaymentStrategy.CreditCardDetails cardDetails = 
            new CreditCardPaymentStrategy.CreditCardDetails(
                "4532123456789012", "John Doe", "12/25", "123"
            );
        
        System.out.println("Strategy 1 - Credit Card Payment:");
        Payment payment1 = rentalSystem.processPayment(paymentContext, sampleBillId, cardDetails);
        System.out.println("   • " + payment1.getPaymentSummary());
        
        // For demonstration, let's create another bill
        try {
            Reservation reservation2 = rentalSystem.createReservation(
                "U002", "V002", LocalDate.now().plusDays(2), LocalDate.now().plusDays(5), 
                "LOC002", "LOC002"
            );
            Bill bill2 = rentalSystem.generateBill(reservation2.getReservationId());
            
            // Strategy 2: PayPal Payment
            paymentContext.setPaymentStrategy(new PayPalPaymentStrategy());
            PayPalPaymentStrategy.PayPalDetails paypalDetails = 
                new PayPalPaymentStrategy.PayPalDetails("jane.smith@email.com", "securepassword");
            
            System.out.println("\\nStrategy 2 - PayPal Payment:");
            Payment payment2 = rentalSystem.processPayment(paymentContext, bill2.getBillId(), paypalDetails);
            System.out.println("   • " + payment2.getPaymentSummary());
            
            // Strategy 3: Cash Payment
            paymentContext.setPaymentStrategy(new CashPaymentStrategy());
            CashPaymentStrategy.CashDetails cashDetails = 
                new CashPaymentStrategy.CashDetails(200.0, "EMP001", "STORE002");
            
            System.out.println("\\nStrategy 3 - Cash Payment:");
            Payment payment3 = rentalSystem.processPayment(paymentContext, bill2.getBillId(), cashDetails);
            System.out.println("   • " + payment3.getPaymentSummary());
            
        } catch (Exception e) {
            System.out.println("   • Additional payment demonstrations skipped: " + e.getMessage());
        }
        
        System.out.println("✅ Payment Strategy Pattern demonstrated with 3 different methods");
    }
    
    /**
     * Shows comprehensive system statistics.
     */
    private void showSystemStatistics(VehicleRentalSystem rentalSystem) {
        System.out.println("\\n📊 STEP 7: System Statistics...");
        
        Map<String, Object> stats = rentalSystem.getSystemStatistics();
        
        System.out.println("System Overview:");
        System.out.println("   • Total Users: " + stats.get("Total Users"));
        System.out.println("   • Total Stores: " + stats.get("Total Stores"));
        System.out.println("   • Total Vehicles: " + stats.get("Total Vehicles"));
        System.out.println("   • Available Vehicles: " + stats.get("Available Vehicles"));
        System.out.println("   • Total Reservations: " + stats.get("Total Reservations"));
        System.out.println("   • Active Reservations: " + stats.get("Active Reservations"));
        System.out.println("   • Total Bills: " + stats.get("Total Bills"));
        System.out.println("   • Paid Bills: " + stats.get("Paid Bills"));
        System.out.println("   • Total Payments: " + stats.get("Total Payments"));
        
        // Show individual store summaries
        System.out.println("\\nStore Details:");
        for (Store store : rentalSystem.getAllStores()) {
            System.out.println("\\n" + store.getStoreSummary());
        }
        
        System.out.println("✅ System statistics displayed");
    }
}

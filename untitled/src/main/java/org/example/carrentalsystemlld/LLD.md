# Car Rental System - Low Level Design (LLD)

## System Overview
A comprehensive car rental system that allows users to search, book, and rent vehicles from different locations. The system supports multiple payment methods, inventory management, and reservation tracking.

## Business Flow
```
User Registration/Login → Location Selection → Vehicle Search & Filter → 
Vehicle Selection → Reservation Creation → Bill Generation → Payment Processing → 
Booking Confirmation
```

## Design Patterns Used

### 1. **Singleton Pattern**
- **Where**: `VehicleRentalSystem` class
- **Why**: Ensures only one instance of the rental system exists, providing global access point and maintaining system state consistency

### 2. **Strategy Pattern** 
- **Where**: 
  - `VehicleSearchStrategy` for different search algorithms (by type, price range, availability)
  - `PaymentStrategy` for different payment methods (Credit Card, PayPal, Cash)
- **Why**: Allows runtime selection of algorithms and easy addition of new search/payment methods without modifying existing code

### 3. **Repository Pattern**
- **Where**: `VehicleInventoryManagement` class
- **Why**: Abstracts data access layer, provides clean separation between business logic and data persistence

### 4. **Factory Pattern**
- **Where**: `VehicleFactory` for creating different vehicle types
- **Why**: Encapsulates vehicle creation logic and allows easy addition of new vehicle types

### 5. **Observer Pattern** (Optional Enhancement)
- **Where**: Notification system for reservation updates
- **Why**: Decouples notification logic from core business logic

## Core Classes and Relationships

### Enums
```java
public enum VehicleType { CAR, MOTORCYCLE, TRUCK, VAN }
public enum VehicleStatus { AVAILABLE, RESERVED, MAINTENANCE, RETIRED }
public enum ReservationStatus { SCHEDULED, ACTIVE, COMPLETED, CANCELLED }
public enum PaymentStatus { PENDING, COMPLETED, FAILED, REFUNDED }
```

### Class Diagram Structure

#### Vehicle Hierarchy
```
Vehicle (Abstract)
├── Car
├── Motorcycle  
├── Truck
└── Van
```

#### Core Entities
- **User**: Represents system users with personal details and rental history
- **Location**: Geographic location with address details and associated store
- **Store**: Physical rental location with inventory management
- **Vehicle**: Base class for all rentable vehicles
- **Reservation**: Booking details linking user, vehicle, and time period
- **Bill**: Financial record for each reservation
- **Payment**: Payment processing and status tracking

#### Management Classes
- **VehicleRentalSystem**: Main system coordinator (Singleton)
- **VehicleInventoryManagement**: Handles vehicle CRUD operations (Repository)
- **ReservationManager**: Manages booking lifecycle
- **BillCalculator**: Computes rental costs with different pricing strategies

## Detailed Class Specifications

### Vehicle
```java
- vehicleId: String
- vehicleNumber: String  
- vehicleType: VehicleType
- status: VehicleStatus
- dailyRentalCost: double
- model: String
- manufacturingYear: int
- kmDriven: long
```

### Store  
```java
- storeId: String
- location: Location
- inventoryManagement: VehicleInventoryManagement
- reservations: List<Reservation>
```

### Reservation
```java
- reservationId: String
- user: User
- vehicle: Vehicle  
- bookingDate: LocalDateTime
- startDate: LocalDate
- endDate: LocalDate
- pickupLocation: Location
- dropLocation: Location
- status: ReservationStatus
```

### User
```java
- userId: String
- name: String
- email: String
- phone: String
- drivingLicense: String
- address: String
```

## Key Design Decisions

### 1. **Separation of Concerns**
- Vehicle inventory management is separated from reservation management
- Payment processing is abstracted through strategy pattern
- Location and store are separate entities for better flexibility

### 2. **Extensibility**
- New vehicle types can be added easily through factory pattern
- New search criteria can be added through strategy pattern  
- New payment methods can be integrated without core changes

### 3. **Data Consistency**
- Vehicle status is updated atomically during reservation process
- Reservation status tracking prevents double booking
- Bill generation is tied to reservation lifecycle

### 4. **Error Handling**
- Custom exceptions for domain-specific errors
- Validation at service layer boundaries
- Graceful handling of payment failures

## System Capabilities

### User Operations
- Register/Login to system
- Search vehicles by location, type, date range
- Filter results by price, features, availability
- Make reservations with pickup/drop locations
- View booking history and status
- Process payments through multiple methods

### Admin Operations  
- Manage vehicle inventory (CRUD operations)
- View and manage reservations
- Generate reports on utilization and revenue
- Handle vehicle maintenance scheduling

### System Features
- Real-time vehicle availability checking
- Dynamic pricing based on demand and vehicle type
- Multi-location support with vehicle transfers
- Payment processing with multiple strategies
- Comprehensive logging and error handling

## Future Enhancements
- Integration with external payment gateways
- Mobile app support with location services
- Advanced analytics and reporting
- Integration with vehicle maintenance systems
- Customer loyalty programs and discounts


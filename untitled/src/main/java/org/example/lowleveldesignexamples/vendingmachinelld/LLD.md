# Vending Machine - Low Level Design (LLD)

## Overview
This document outlines the Low Level Design for a Vending Machine system using the State Design Pattern. The vending machine manages product inventory, accepts cash, processes product selection, and handles dispensing with proper state transitions.

## Requirements
- Product management with codes and prices
- Cash insertion and validation
- Product selection by code
- Cancel operation with refund
- Product dispensing
- Change calculation and return
- State-based operation flow

## State Design Pattern

### What is State Design Pattern?
The State Design Pattern is a behavioral design pattern that allows an object to change its behavior when its internal state changes. The object appears to change its class when the state changes.

**Key Benefits:**
- **Single Responsibility**: Each state handles its own behavior
- **Open/Closed Principle**: Easy to add new states without modifying existing code
- **Eliminates Complex Conditionals**: No need for large if-else or switch statements
- **Clear State Transitions**: Explicit state management and transitions

### Why Use State Pattern for Vending Machine?
Vending machines have distinct operational states with different behaviors:
- Different button responses based on current state
- Clear state transitions (Idle → HasMoney → ProductSelected → Dispensing)
- Each state has specific validation rules
- Error handling varies by state

## State Diagram

```
    [START]
       ↓
   ┌─────────┐
   │  IDLE   │ ←──────────────────┐
   └─────────┘                    │
       ↓ insertCash()             │
   ┌─────────┐                    │
   │HAS_MONEY│                    │
   └─────────┘                    │
       ↓ selectProduct()          │
   ┌─────────┐                    │
   │PRODUCT_ │                    │
   │SELECTED │                    │
   └─────────┘                    │
       ↓ dispenseProduct()        │
   ┌─────────┐                    │
   │DISPENSING│                   │
   └─────────┘                    │
       ↓ complete()               │
       └────────────────────────────┘
       
   From any state: cancel() → IDLE (with refund)
```

## State Transition Table

| Current State | Action | Next State | Conditions | Side Effects |
|---------------|--------|------------|------------|--------------|
| IDLE | insertCash() | HAS_MONEY | cash > 0 | Store cash amount |
| IDLE | selectProduct() | IDLE | - | Show "Insert cash first" |
| IDLE | cancel() | IDLE | - | No action needed |
| HAS_MONEY | insertCash() | HAS_MONEY | cash > 0 | Add to existing cash |
| HAS_MONEY | selectProduct() | PRODUCT_SELECTED | valid code & sufficient funds | Reserve product |
| HAS_MONEY | selectProduct() | HAS_MONEY | insufficient funds | Show error message |
| HAS_MONEY | cancel() | IDLE | - | Refund all cash |
| PRODUCT_SELECTED | dispenseProduct() | DISPENSING | - | Start dispensing mechanism |
| PRODUCT_SELECTED | cancel() | IDLE | - | Refund cash, unreserve product |
| DISPENSING | complete() | IDLE | - | Calculate change, reset machine |
| DISPENSING | error() | HAS_MONEY | dispensing failed | Unreserve product, keep cash |

## Class Design

### Core Classes

#### 1. Product Class
```java
public class Product {
    private String code;
    private String name;
    private double price;
    private int quantity;
    
    // Constructor, getters, setters
}
```

#### 2. VendingMachine Class (Context)
```java
public class VendingMachine {
    private VendingMachineState currentState;
    private double insertedCash;
    private Map<String, Product> inventory;
    private Product selectedProduct;
    
    public VendingMachine() {
        this.currentState = new IdleState();
        this.inventory = new HashMap<>();
        this.insertedCash = 0.0;
    }
    
    // State delegation methods
    public void insertCash(double amount) {
        currentState.insertCash(this, amount);
    }
    
    public void selectProduct(String productCode) {
        currentState.selectProduct(this, productCode);
    }
    
    public void cancel() {
        currentState.cancel(this);
    }
    
    public void dispenseProduct() {
        currentState.dispenseProduct(this);
    }
    
    // State management
    public void setState(VendingMachineState state) {
        this.currentState = state;
    }
    
    // Helper methods
    public void addCash(double amount) { this.insertedCash += amount; }
    public void resetCash() { this.insertedCash = 0.0; }
    public double getInsertedCash() { return insertedCash; }
    // ... other helper methods
}
```

#### 3. VendingMachineState Interface
```java
public interface VendingMachineState {
    void insertCash(VendingMachine machine, double amount);
    void selectProduct(VendingMachine machine, String productCode);
    void cancel(VendingMachine machine);
    void dispenseProduct(VendingMachine machine);
    String getStateName();
}
```

### State Implementations

#### 4. IdleState
```java
public class IdleState implements VendingMachineState {
    
    @Override
    public void insertCash(VendingMachine machine, double amount) {
        if (amount > 0) {
            machine.addCash(amount);
            machine.setState(new HasMoneyState());
            System.out.println("Cash inserted: $" + amount);
        } else {
            System.out.println("Please insert valid amount");
        }
    }
    
    @Override
    public void selectProduct(VendingMachine machine, String productCode) {
        System.out.println("Please insert cash first");
    }
    
    @Override
    public void cancel(VendingMachine machine) {
        System.out.println("Nothing to cancel");
    }
    
    @Override
    public void dispenseProduct(VendingMachine machine) {
        System.out.println("Please insert cash and select product first");
    }
    
    @Override
    public String getStateName() {
        return "IDLE";
    }
}
```

#### 5. HasMoneyState
```java
public class HasMoneyState implements VendingMachineState {
    
    @Override
    public void insertCash(VendingMachine machine, double amount) {
        if (amount > 0) {
            machine.addCash(amount);
            System.out.println("Additional cash inserted: $" + amount);
            System.out.println("Total cash: $" + machine.getInsertedCash());
        }
    }
    
    @Override
    public void selectProduct(VendingMachine machine, String productCode) {
        Product product = machine.getProduct(productCode);
        
        if (product == null) {
            System.out.println("Invalid product code");
            return;
        }
        
        if (product.getQuantity() <= 0) {
            System.out.println("Product out of stock");
            return;
        }
        
        if (machine.getInsertedCash() < product.getPrice()) {
            System.out.println("Insufficient funds. Need $" + 
                (product.getPrice() - machine.getInsertedCash()) + " more");
            return;
        }
        
        machine.setSelectedProduct(product);
        machine.setState(new ProductSelectedState());
        System.out.println("Product selected: " + product.getName());
    }
    
    @Override
    public void cancel(VendingMachine machine) {
        double refund = machine.getInsertedCash();
        machine.resetCash();
        machine.setState(new IdleState());
        System.out.println("Transaction cancelled. Refunded: $" + refund);
    }
    
    @Override
    public void dispenseProduct(VendingMachine machine) {
        System.out.println("Please select a product first");
    }
    
    @Override
    public String getStateName() {
        return "HAS_MONEY";
    }
}
```

#### 6. ProductSelectedState
```java
public class ProductSelectedState implements VendingMachineState {
    
    @Override
    public void insertCash(VendingMachine machine, double amount) {
        System.out.println("Product already selected. Processing...");
        // Auto-trigger dispensing
        dispenseProduct(machine);
    }
    
    @Override
    public void selectProduct(VendingMachine machine, String productCode) {
        System.out.println("Product already selected. Cancel to select different product");
    }
    
    @Override
    public void cancel(VendingMachine machine) {
        double refund = machine.getInsertedCash();
        machine.resetCash();
        machine.setSelectedProduct(null);
        machine.setState(new IdleState());
        System.out.println("Selection cancelled. Refunded: $" + refund);
    }
    
    @Override
    public void dispenseProduct(VendingMachine machine) {
        machine.setState(new DispensingState());
        System.out.println("Dispensing " + machine.getSelectedProduct().getName() + "...");
        
        // Simulate dispensing process
        try {
            Thread.sleep(2000); // Simulate dispensing time
            completeDispensing(machine);
        } catch (InterruptedException e) {
            handleDispensingError(machine);
        }
    }
    
    private void completeDispensing(VendingMachine machine) {
        Product product = machine.getSelectedProduct();
        
        // Update inventory
        product.setQuantity(product.getQuantity() - 1);
        
        // Calculate change
        double change = machine.getInsertedCash() - product.getPrice();
        
        // Reset machine
        machine.resetCash();
        machine.setSelectedProduct(null);
        machine.setState(new IdleState());
        
        System.out.println("Product dispensed successfully!");
        if (change > 0) {
            System.out.println("Change returned: $" + change);
        }
    }
    
    private void handleDispensingError(VendingMachine machine) {
        System.out.println("Dispensing failed. Please try again.");
        machine.setState(new HasMoneyState());
    }
    
    @Override
    public String getStateName() {
        return "PRODUCT_SELECTED";
    }
}
```

#### 7. DispensingState
```java
public class DispensingState implements VendingMachineState {
    
    @Override
    public void insertCash(VendingMachine machine, double amount) {
        System.out.println("Currently dispensing. Please wait...");
    }
    
    @Override
    public void selectProduct(VendingMachine machine, String productCode) {
        System.out.println("Currently dispensing. Please wait...");
    }
    
    @Override
    public void cancel(VendingMachine machine) {
        System.out.println("Cannot cancel during dispensing");
    }
    
    @Override
    public void dispenseProduct(VendingMachine machine) {
        System.out.println("Already dispensing...");
    }
    
    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}
```

## Usage Example

```java
public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        
        // Initialize inventory
        machine.addProduct("A1", new Product("A1", "Coke", 1.50, 10));
        machine.addProduct("B1", new Product("B1", "Chips", 2.00, 5));
        
        // Sunny day flow
        System.out.println("=== Sunny Day Flow ===");
        machine.insertCash(2.00);           // IDLE → HAS_MONEY
        machine.selectProduct("A1");        // HAS_MONEY → PRODUCT_SELECTED
        machine.dispenseProduct();          // PRODUCT_SELECTED → DISPENSING → IDLE
        
        System.out.println("\n=== Cancel Flow ===");
        machine.insertCash(1.00);           // IDLE → HAS_MONEY
        machine.cancel();                   // HAS_MONEY → IDLE (with refund)
        
        System.out.println("\n=== Error Handling ===");
        machine.selectProduct("A1");        // IDLE: "Insert cash first"
        machine.insertCash(1.00);           // IDLE → HAS_MONEY
        machine.selectProduct("B1");        // HAS_MONEY: "Insufficient funds"
    }
}
```

## Design Benefits

### 1. Maintainability
- Each state class handles only its specific behavior
- Easy to modify state logic without affecting others
- Clear separation of concerns

### 2. Extensibility
- New states can be added easily (e.g., MaintenanceState)
- New behaviors per state without modifying existing code
- Payment method extensions (card, mobile payments)

### 3. Testability
- Each state can be unit tested independently
- Mock states for testing specific scenarios
- Clear test cases per state transition

### 4. Reliability
- Prevents invalid operations in wrong states
- Consistent state management
- Error handling per state context

## Alternative Approaches Considered

### 1. If-Else Approach
```java
// NOT RECOMMENDED
public void selectProduct(String code) {
    if (currentState == IDLE) {
        System.out.println("Insert cash first");
    } else if (currentState == HAS_MONEY) {
        // product selection logic
    } else if (currentState == DISPENSING) {
        System.out.println("Already dispensing");
    }
    // ... becomes unwieldy with more states
}
```

**Problems:**
- Violates Open/Closed Principle
- Complex nested conditions
- Difficult to maintain and extend
- Scattered state logic

### 2. Enum with Switch
```java
// BETTER BUT NOT IDEAL
public enum MachineState {
    IDLE, HAS_MONEY, PRODUCT_SELECTED, DISPENSING
}

public void selectProduct(String code) {
    switch(currentState) {
        case IDLE:
            System.out.println("Insert cash first");
            break;
        case HAS_MONEY:
            // logic here
            break;
        // ... other cases
    }
}
```

**Problems:**
- All logic in one class
- Still violates Single Responsibility
- Switch statements scattered across methods

## Conclusion

The State Design Pattern provides an elegant solution for the Vending Machine's state-dependent behavior. It ensures clean code organization, easy maintenance, and robust state management while handling complex business rules and error scenarios effectively.

The pattern's strength lies in its ability to encapsulate state-specific behavior, making the system more predictable and easier to debug, extend, and maintain over time.
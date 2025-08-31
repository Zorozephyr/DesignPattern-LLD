# Design Patterns Implementation in Java

This project demonstrates the implementation of various design patterns in Java. Each pattern is implemented with real-world examples to showcase their practical applications.

## 📋 Table of Contents
- [Overview](#overview)
- [Design Patterns Implemented](#design-patterns-implemented)
  - [1. Factory Pattern](#1-factory-pattern)
  - [2. Observer Pattern](#2-observer-pattern)
  - [3. Strategy Pattern](#3-strategy-pattern)
  - [4. Decorator Pattern](#4-decorator-pattern)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [Requirements](#requirements)

## Overview

This project serves as a comprehensive demonstration of common design patterns used in software development. Each pattern is implemented with practical examples that solve real-world problems, making it easier to understand when and how to apply these patterns in your own projects.

## Design Patterns Implemented

### 1. Factory Pattern 🏭

**Location:** `src/main/java/org/example/factorypattern/`

**Purpose:** Creates objects without specifying the exact class to create. This pattern is useful when you need to create different types of objects based on certain conditions.

**Implementation Details:**
- **Abstract Factory:** `GUIFactory` interface defines the contract for creating UI components
- **Concrete Factories:** 
  - `WindowsFactory` - Creates Windows-style UI components
  - `MacFactory` - Creates Mac-style UI components
- **Products:** 
  - Button components (`WindowsButton`, `MacButton`)
  - CheckBox components (`WindowsCheckBox`, `MacCheckBox`)

**Real-world Example:** Cross-platform GUI application that creates platform-specific UI components based on the operating system.

**How to Run:**
```bash
java org.example.factorypattern.Main windows
# or
java org.example.factorypattern.Main mac
```

**Key Benefits:**
- Encapsulates object creation logic
- Makes the code more flexible and maintainable
- Follows the Open/Closed Principle
- Reduces coupling between client code and concrete classes

### 2. Observer Pattern 👁️

**Location:** `src/main/java/org/example/observerpattern/`

**Purpose:** Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

**Implementation Details:**
- **Subject:** `StockServer` - Maintains stock prices and notifies observers of changes
- **Observer Interface:** `StockObserver` - Defines the update method for observers
- **Concrete Observer:** `WebDashboard` - Receives and displays stock price updates
- **Features:**
  - Thread-safe implementation using `CopyOnWriteArrayList`
  - Price history tracking with bounded history (last 10 prices)
  - Missed updates tracking for reconnecting clients

**Real-world Example:** Stock market application where multiple clients (web dashboard, mobile app) need to be notified of stock price changes in real-time.

**How to Run:**
```bash
java org.example.observerpattern.StockMarketApp
```

**Key Benefits:**
- Loose coupling between subject and observers
- Dynamic relationships - observers can be added/removed at runtime
- Broadcast communication capability
- Supports the principle of separation of concerns

### 3. Strategy Pattern 🎯

**Location:** `src/main/java/org/example/strategypattern/`

**Purpose:** Defines a family of algorithms, encapsulates each one, and makes them interchangeable. This pattern lets the algorithm vary independently from clients that use it.

**Implementation Details:**
- **Strategy Interface:** `DiscountStrategy` - Defines the contract for discount calculation
- **Concrete Strategy:** `PercentageDiscount` - Implements percentage-based discount calculation
- **Context:** `Order` - Uses a discount strategy to calculate final amount
- **Extensible:** Easy to add new discount strategies (e.g., fixed amount, buy-one-get-one, seasonal discounts)

**Real-world Example:** E-commerce order processing system with different discount strategies that can be applied based on customer type, season, or promotion.

**How to Run:**
```bash
java org.example.strategypattern.Order
```

**Key Benefits:**
- Eliminates conditional statements for algorithm selection
- Makes algorithms interchangeable at runtime
- Easy to extend with new strategies
- Follows the Open/Closed Principle

### 4. Decorator Pattern 🎨

**Location:** `src/main/java/org/example/decoratorpattern/`

**Purpose:** Allows behavior to be added to individual objects, either statically or dynamically, without affecting the behavior of other objects from the same class.

**Implementation Details:**
- **Component Interface:** `Coffee` - Defines the contract for coffee types
- **Concrete Component:** `SimpleCoffee` - Implements a basic coffee
- **Decorator Abstract Class:** `CoffeeDecorator` - Implements the coffee interface and contains a reference to a coffee object
- **Concrete Decorators:** 
  - `MilkDecorator` - Adds milk to the coffee
  - `SugarDecorator` - Adds sugar to the coffee
  - `WhipDecorator` - Adds whipped cream to the coffee

**Real-world Example:** Coffee shop application where different condiments (milk, sugar, whipped cream) can be added to coffee orders dynamically.

**How to Run:**
```bash
java org.example.decoratorpattern.CoffeeShop
```

**Key Benefits:**
- Flexible alternative to subclassing for extending functionality
- Behaviors can be added/removed at runtime
- Supports the Single Responsibility Principle by dividing functionality into classes

## Project Structure

```
src/main/java/org/example/
├── factorypattern/
│   ├── Main.java                    # Entry point for Factory pattern demo
│   ├── Gui.java                     # GUI context class
│   ├── factory/
│   │   ├── GUIFactory.java          # Abstract factory interface
│   │   ├── WindowsFactory.java      # Windows UI factory
│   │   └── MacFactory.java          # Mac UI factory
│   └── uicomponents/
│       ├── button/
│       │   ├── ButtonGui.java       # Button interface
│       │   ├── WindowsButton.java   # Windows button implementation
│       │   └── MacButton.java       # Mac button implementation
│       └── checkBox/
│           ├── checkBoxGui.java     # CheckBox interface
│           ├── WindowsCheckBox.java # Windows checkbox implementation
│           └── MacCheckBox.java     # Mac checkbox implementation
├── observerpattern/
│   ├── StockMarketApp.java         # Entry point for Observer pattern demo
│   ├── observable/
│   │   └── StockServer.java        # Subject implementation
│   └── observer/
│       ├── StockObserver.java      # Observer interface
│       └── WebDashboard.java       # Concrete observer implementation
├── strategypattern/
│   ├── Order.java                  # Context class and entry point
│   └── discountstrategy/
│       ├── DiscountStrategy.java   # Strategy interface
│       └── PercentageDiscount.java # Concrete strategy implementation
└── decoratorpattern/
    ├── CoffeeShop.java             # Entry point for Decorator pattern demo
    ├── coffee/
    │   ├── Coffee.java             # Coffee component interface
    │   ├── SimpleCoffee.java       # Concrete component
    │   └── decorators/
    │       ├── CoffeeDecorator.java # Decorator abstract class
    │       ├── MilkDecorator.java   # Concrete decorator for milk
    │       ├── SugarDecorator.java  # Concrete decorator for sugar
    │       └── WhipDecorator.java   # Concrete decorator for whipped cream
```

## How to Run

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Building the Project
```bash
mvn clean compile
```

### Running Individual Patterns

**Factory Pattern:**
```bash
cd target/classes
java org.example.factorypattern.Main windows
java org.example.factorypattern.Main mac
```

**Observer Pattern:**
```bash
cd target/classes
java org.example.observerpattern.StockMarketApp
```

**Strategy Pattern:**
```bash
cd target/classes
java org.example.strategypattern.Order
```

**Decorator Pattern:**
```bash
cd target/classes
java org.example.decoratorpattern.CoffeeShop
```

## Requirements

- **Java Version:** 11+
- **Build Tool:** Maven
- **Dependencies:** No external dependencies required (uses only Java standard library)

## Key Learning Points

1. **Factory Pattern:** Learn how to create objects without tightly coupling your code to specific classes
2. **Observer Pattern:** Understand how to implement event-driven architectures and maintain loose coupling
3. **Strategy Pattern:** See how to make algorithms interchangeable and extend functionality without modifying existing code
4. **Decorator Pattern:** Discover how to add responsibilities to objects dynamically and transparently, without affecting other objects

## Future Enhancements

Potential patterns to add:
- Singleton Pattern
- Command Pattern
- Adapter Pattern
- Builder Pattern

---

**Author:** [Your Name]  
**Date:** August 2025  
**Purpose:** Educational demonstration of design patterns in Java

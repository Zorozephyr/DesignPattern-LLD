package org.example.decoratorpattern.coffee;

/**
 * Concrete Component - implements the basic coffee functionality
 */
public class SimpleCoffee implements Coffee {

    @Override
    public String getDescription() {
        return "Simple Coffee";
    }

    @Override
    public double getCost() {
        return 2.00; // Base price for simple coffee
    }
}

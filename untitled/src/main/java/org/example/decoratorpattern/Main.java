package org.example.decoratorpattern;

import org.example.decoratorpattern.coffee.Coffee;
import org.example.decoratorpattern.coffee.SimpleCoffee;
import org.example.decoratorpattern.coffee.decorators.MilkDecorator;
import org.example.decoratorpattern.coffee.decorators.SugarDecorator;
import org.example.decoratorpattern.coffee.decorators.WhipDecorator;

/**
 * Coffee Shop Demo - Demonstrates the Decorator Pattern
 * Shows how to dynamically add condiments to coffee orders
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Welcome to the Coffee Shop ===");
        System.out.println("Demonstrating Decorator Pattern\n");

        // Order 1: Simple Coffee
        Coffee coffee1 = new SimpleCoffee();
        printOrder("Order 1", coffee1);

        // Order 2: Coffee with Milk
        Coffee coffee2 = new MilkDecorator(new SimpleCoffee());
        printOrder("Order 2", coffee2);

        // Order 3: Coffee with Milk and Sugar
        Coffee coffee3 = new SugarDecorator(
                            new MilkDecorator(
                                new SimpleCoffee()
                            )
                         );
        printOrder("Order 3", coffee3);

        // Order 4: Coffee with all condiments (Milk, Sugar, and Whipped Cream)
        Coffee coffee4 = new WhipDecorator(
                            new SugarDecorator(
                                new MilkDecorator(
                                    new SimpleCoffee()
                                )
                            )
                         );
        printOrder("Order 4", coffee4);

        // Order 5: Double milk coffee (showing decorators can be applied multiple times)
        Coffee coffee5 = new MilkDecorator(
                            new MilkDecorator(
                                new SimpleCoffee()
                            )
                         );
        printOrder("Order 5", coffee5);

        System.out.println("\n=== Key Benefits of Decorator Pattern ===");
        System.out.println("✓ Add responsibilities dynamically");
        System.out.println("✓ Flexible alternative to subclassing");
        System.out.println("✓ Can combine decorators in any order");
        System.out.println("✓ Open/Closed Principle - open for extension, closed for modification");
    }

    private static void printOrder(String orderName, Coffee coffee) {
        System.out.printf("%-10s: %-35s | Cost: $%.2f%n",
                         orderName,
                         coffee.getDescription(),
                         coffee.getCost());
    }
}

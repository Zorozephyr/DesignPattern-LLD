package org.example.designpatterns.decoratorpattern.coffee.decorators;

import org.example.designpatterns.decoratorpattern.coffee.Coffee;

/**
 * Concrete Decorator - adds milk to coffee
 */
public class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.50; // Add $0.50 for milk
    }
}

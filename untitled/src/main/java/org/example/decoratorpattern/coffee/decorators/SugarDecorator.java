package org.example.decoratorpattern.coffee.decorators;

import org.example.decoratorpattern.coffee.Coffee;

/**
 * Concrete Decorator - adds sugar to coffee
 */
public class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Sugar";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.25; // Add $0.25 for sugar
    }
}

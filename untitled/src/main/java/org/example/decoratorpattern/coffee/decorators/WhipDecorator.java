package org.example.decoratorpattern.coffee.decorators;

import org.example.decoratorpattern.coffee.Coffee;

/**
 * Concrete Decorator - adds whipped cream to coffee
 */
public class WhipDecorator extends CoffeeDecorator {

    public WhipDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Whipped Cream";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.75; // Add $0.75 for whipped cream
    }
}

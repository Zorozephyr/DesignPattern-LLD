package org.example.designpatterns.decoratorpattern.coffee.decorators;

import org.example.designpatterns.decoratorpattern.coffee.Coffee;

/**
 * Abstract Decorator - base class for all coffee decorators
 * Contains a reference to a Coffee object and implements the Coffee interface
 */
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription();
    }

    @Override
    public double getCost() {
        return coffee.getCost();
    }
}

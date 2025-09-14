package org.example.designpatterns.strategypattern.discountstrategy;

public class PercentageDiscount implements DiscountStrategy{

    private int discountPercentage;

    public PercentageDiscount(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Integer calculateDiscountedPrice(Integer amount) {
        return amount - (amount*discountPercentage/100);
    }
}

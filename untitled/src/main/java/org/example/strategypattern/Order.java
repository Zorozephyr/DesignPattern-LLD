package org.example.strategypattern;

import org.example.strategypattern.discountstrategy.DiscountStrategy;
import org.example.strategypattern.discountstrategy.PercentageDiscount;

public class Order {

    int item_total;

    DiscountStrategy discountStrategy;

    public Order(int item_total,DiscountStrategy discountStrategy) {
        this.item_total = item_total;
        this.discountStrategy = discountStrategy;
    }

    public int getFinalAmount(){
        return discountStrategy.calculateDiscountedPrice(item_total);
    }

    public static void main(String[] args){

        Order order = new Order(50, new PercentageDiscount(25));
        System.out.println(order.getFinalAmount());
    }
}

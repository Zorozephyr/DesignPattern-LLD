package org.example.observerpattern.observer;

public interface StockObserver {
     void update(String stockSymbol, double price);
}

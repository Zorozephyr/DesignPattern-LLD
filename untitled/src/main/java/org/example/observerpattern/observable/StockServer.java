package org.example.observerpattern.observable;

import org.example.observerpattern.observer.StockObserver;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class StockServer {
    private Map<String, Double> stockPrices = new HashMap<>();
    private List<StockObserver> observers = new CopyOnWriteArrayList<>();
    private Map<String, Queue<Double>> priceHistory = new HashMap<>(); // Bounded history
    private final int MAX_HISTORY = 10;

    // Register observer
    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    // Remove observer
    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    // Update stock price and notify observers
    public synchronized void setStockPrice(String stockSymbol, double price) {
        stockPrices.put(stockSymbol, price);

        // Add to history
        priceHistory.computeIfAbsent(stockSymbol, k -> new LinkedList<>()).add(price);
        if (priceHistory.get(stockSymbol).size() > MAX_HISTORY) {
            priceHistory.get(stockSymbol).poll();
        }

        // Notify observers
        for (StockObserver observer : observers) {
            observer.update(stockSymbol, price);
        }
    }

    // Replay missed updates for a reconnected client
    public synchronized List<Double> getMissedUpdates(String stockSymbol) {
        return new ArrayList<>(priceHistory.getOrDefault(stockSymbol, new LinkedList<>()));
    }
}
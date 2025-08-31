package org.example.observerpattern;

import org.example.observerpattern.observable.StockServer;

import org.example.observerpattern.observer.StockObserver;
import org.example.observerpattern.observer.WebDashboard;

public class StockMarketApp {
    public static void main(String[] args) {
        StockServer server = new StockServer();

        // Create observers
        StockObserver web = new WebDashboard("Web Dashboard");
        StockObserver mobile = new WebDashboard("Mobile App");

        // Register observers
        server.addObserver(web);
        server.addObserver(mobile);

        // Simulate stock price updates
        server.setStockPrice("AAPL", 150.25);
        server.setStockPrice("GOOGL", 2800.50);

        // Simulate reconnection of a client
        System.out.println("Missed updates for AAPL: " + server.getMissedUpdates("AAPL"));
    }
}

package org.example.designpatterns.observerpattern.observer;



public class WebDashboard implements StockObserver {
    private String name;

    public WebDashboard(String name) {
        this.name = name;
    }

    @Override
    public void update(String stockSymbol, double price) {
        System.out.println(name + " received update: " + stockSymbol + " = $" + price);
    }
}

package edu.upc.dsa.models;

import java.util.List;

public class Order {

    private String userId;
    private List<OrderItem> items;
    private boolean served;

    public Order(String userId, List<OrderItem> items) {
        this.userId = userId;
        this.items = items;
        this.served = false;
    }
    public List<OrderItem> getProducts() {
        return items;
    }

    public void setProducts(List<OrderItem> products) {
        this.items = products;
    }

    // Getters y setters
    public String getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }

    @Override
    public String toString() {
        return "Order{userId='" + userId + "', items=" + items + ", served=" + served + "}";
    }

}



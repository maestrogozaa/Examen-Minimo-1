package edu.upc.dsa.models;

public class OrderItem {

    private String productId;
    private int quantity;

    public OrderItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{productId='" + productId + "', quantity=" + quantity + "}";
    }
}



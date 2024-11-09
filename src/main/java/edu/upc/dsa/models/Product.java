package edu.upc.dsa.models;

public class Product {

    private String id;
    private String name;
    private double price;
    private int sales;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sales = 0;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getSales() {
        return sales;
    }

    public void incrementSales(int quantity) {
        this.sales += quantity;
    }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price + ", sales=" + sales + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }
}



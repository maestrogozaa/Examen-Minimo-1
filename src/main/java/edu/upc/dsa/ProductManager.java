package edu.upc.dsa;

import edu.upc.dsa.models.*;

import java.util.List;

public interface ProductManager {
    List<Order> getUserOrders(String userId);

    List<Product> findAllSortedBySales();

    List<Product> getProductsSortedByPrice();
    void placeOrder(String userId, List<OrderItem> items);
    List<Product> findAll();
    List<Product> findAllSortedByPrice();

    void createOrder(Order order);

    Order serveNextOrder();
    List<Order> getOrdersByUser(String userId);
    List<Product> getProductsSortedBySales();

    void clearData();

    void addProduct(Product productoA);

    List<Order> getPendingOrders();
}

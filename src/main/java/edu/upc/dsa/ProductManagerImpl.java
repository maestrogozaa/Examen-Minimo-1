package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.util.*;

public class ProductManagerImpl implements ProductManager {

    private static ProductManagerImpl instance;
    private List<Product> products;
    private Queue<Order> orderQueue;
    private Map<String, List<Order>> userOrders;
    private final static Logger logger = Logger.getLogger(ProductManagerImpl.class);
    private Queue<Order> pendingOrders;

    private ProductManagerImpl() {
        this.products = new ArrayList<>();
        addProduct(new Product("1", "Producto A", 10.0));
        addProduct(new Product("2", "Producto B", 20.0));
        addProduct(new Product("3", "Producto C", 5.0));
        this.orderQueue = new LinkedList<>();
        this.userOrders = new HashMap<>();
    }

    public static ProductManagerImpl getInstance() {
        if (instance == null) {
            instance = new ProductManagerImpl();
            logger.info("ProductManagerImpl instance created.");
        }
        return instance;
    }

    // Método para agregar un producto (útil para pruebas)
    public void addProduct(Product product) {
        this.products.add(product);
        logger.info("Producto añadido: " + product);
    }

    // Método para limpiar los datos (útil para pruebas)
    public void clearData() {
        this.products.clear();
        this.pendingOrders.clear();
        this.userOrders.clear();
        logger.info("Datos limpiados");
    }

    // Método para obtener los pedidos pendientes
    public List<Order> getPendingOrders() {
        return new ArrayList<>(this.pendingOrders);
    }

    @Override
    public List<Product> findAll() {
        logger.info("findAll() called.");
        return new ArrayList<>(products);
    }

    @Override
    public List<Product> findAllSortedByPrice() {
        logger.info("findAllSortedByPrice() called.");
        products.sort(Comparator.comparingDouble(Product::getPrice));
        return new ArrayList<>(products);
    }

    @Override
    public void createOrder(Order order) {
        logger.info("createOrder() called with: " + order);
        if (order.getUserId() == null || order.getItems() == null || order.getItems().isEmpty()) {
            logger.error("Invalid order parameters: " + order);
            return;
        }
        orderQueue.add(order);
        userOrders.computeIfAbsent(order.getUserId(), k -> new ArrayList<>()).add(order);
        logger.info("Order created successfully: " + order);
    }

    @Override
    public Order serveNextOrder() {
        logger.info("serveNextOrder() called.");
        Order order = orderQueue.poll();
        if (order == null) {
            logger.warn("No orders to serve.");
            return null;
        }

        for (OrderItem item : order.getItems()) {
            Product product = findProductById(item.getProductId());
            if (product != null) {
                product.incrementSales(item.getQuantity());
            } else {
                logger.error("Product not found: " + item.getProductId());
            }
        }
        order.setServed(true);
        logger.info("Order served: " + order);
        return order;
    }

    @Override
    public List<Order> getUserOrders(String userId) {
        logger.info("getUserOrders() called for user: " + userId);
        return userOrders.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public List<Product> findAllSortedBySales() {
        logger.info("findAllSortedBySales() called.");
        products.sort(Comparator.comparingInt(Product::getSales).reversed());
        return new ArrayList<>(products);
    }

    private Product findProductById(String productId) {
        logger.info("findProductById() called with productId: " + productId);
        return products.stream().filter(p -> p.getId().equals(productId)).findFirst().orElse(null);
    }

    // Métodos adicionales
    @Override
    public List<Product> getProductsSortedByPrice() {
        return findAllSortedByPrice();
    }

    @Override
    public void placeOrder(String userId, List<OrderItem> items) {
        Order order = new Order(userId, items);
        createOrder(order);
    }

    @Override
    public List<Order> getOrdersByUser(String userId) {
        return getUserOrders(userId);
    }

    @Override
    public List<Product> getProductsSortedBySales() {
        return findAllSortedBySales();
    }
}

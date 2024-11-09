package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductManagerTest {

    private ProductManager pm;

    @Before
    public void setUp() {
        // Inicializamos la instancia de ProductManager y los datos de prueba
        pm = ProductManagerImpl.getInstance();

        // Añadir algunos productos de prueba
        pm.addProduct(new Product("1", "Producto A", 10.0));
        pm.addProduct(new Product("2", "Producto B", 20.0));
        pm.addProduct(new Product("3", "Producto C", 5.0));
    }

    @After
    public void tearDown() {
        // Liberamos los recursos, como restablecer la instancia singleton
        pm.clearData();
    }

    @Test
    public void testPlaceOrder() {
        // Crear una lista de elementos de pedido
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("1", 2)); // 2 unidades de Producto A
        items.add(new OrderItem("3", 1)); // 1 unidad de Producto C

        // Realizar el pedido
        pm.placeOrder("user1", items);

        // Verificar que el pedido se ha agregado a la cola de pedidos pendientes
        List<Order> pendingOrders = pm.getPendingOrders();
        assertFalse(pendingOrders.isEmpty());
        assertEquals("user1", pendingOrders.get(0).getUserId());
        assertEquals(2, pendingOrders.get(0).getItems().size());
    }

    @Test
    public void testServeOrder() {
        // Crear y colocar un pedido para ser servido
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("2", 1)); // 1 unidad de Producto B
        pm.placeOrder("user2", items);

        // Verificar que el pedido esté en la cola de pendientes
        assertFalse(pm.getPendingOrders().isEmpty());

        // Servir el pedido
        Order servedOrder = pm.serveNextOrder();

        // Verificar que el pedido ha sido servido correctamente
        assertNotNull(servedOrder);
        assertEquals("user2", servedOrder.getUserId());
        assertTrue(servedOrder.isServed());

        // Verificar que la cola de pendientes esté vacía después de servir
        assertTrue(pm.getPendingOrders().isEmpty());
    }
}

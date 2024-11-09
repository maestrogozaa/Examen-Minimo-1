package edu.upc.dsa.services;

import edu.upc.dsa.*;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Api(value = "/products", description = "Endpoint to Track Service")
@Path("/products")
public class ProductService {

    private ProductManager pm;
    private final static Logger logger = Logger.getLogger(ProductService.class);

    public ProductService() {
        this.pm = ProductManagerImpl.getInstance();
    }

    private <T> Response createListResponse(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Response.status(404).entity("No products found").build();
        }
        GenericEntity<List<T>> entity = new GenericEntity<List<T>>(list) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all products", notes = "Retrieve all products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        try {
            List<Product> products = this.pm.findAll();
            return createListResponse(products);
        } catch (Exception e) {
            // Log the exception
            logger.error("Error al obtener los productos", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @GET
    @ApiOperation(value = "get all products sorted by price", notes = "List products ordered by price in ascending order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer="List")
    })
    @Path("/sortedByPrice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByPrice() {
        try {
            List<Product> products = this.pm.findAllSortedByPrice();
            return createListResponse(products);
        } catch (Exception e) {
            // Log the exception
            logger.error("Error al obtener productos ordenados por precio", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @POST
    @ApiOperation(value = "create a new order", notes = "Make an order with different products and quantities")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Order Created", response = Order.class),
            @ApiResponse(code = 400, message = "Invalid order details")
    })
    @Path("/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(Order order) {
        if (order.getUserId() == null || order.getProducts().isEmpty()) {
            return Response.status(400).entity("Invalid order details").build();
        }
        try {
            this.pm.createOrder(order);
            return Response.status(201).entity(order).build();
        } catch (Exception e) {
            logger.error("Error al crear el pedido", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @POST
    @ApiOperation(value = "serve an order", notes = "Serve an order in FIFO order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order Served", response = Order.class),
            @ApiResponse(code = 404, message = "No Orders to Serve")
    })
    @Path("/orders/serve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response serveOrder() {
        try {
            Order order = this.pm.serveNextOrder();
            if (order == null) {
                return Response.status(404).entity("No orders to serve").build();
            }
            return Response.status(200).entity(order).build();
        } catch (Exception e) {
            logger.error("Error al servir el pedido", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @GET
    @ApiOperation(value = "get user orders", notes = "List all completed orders for a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Order.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "No Orders Found")
    })
    @Path("/orders/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserOrders(@PathParam("userId") String userId) {
        try {
            List<Order> orders = this.pm.getUserOrders(userId);
            return createListResponse(orders);
        } catch (Exception e) {
            logger.error("Error al obtener los pedidos del usuario", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @GET
    @ApiOperation(value = "get products sorted by sales", notes = "List products ordered by number of sales in descending order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer="List")
    })
    @Path("/sortedBySales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsBySales() {
        try {
            List<Product> products = this.pm.findAllSortedBySales();
            return createListResponse(products);
        } catch (Exception e) {
            logger.error("Error al obtener productos ordenados por ventas", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }
}

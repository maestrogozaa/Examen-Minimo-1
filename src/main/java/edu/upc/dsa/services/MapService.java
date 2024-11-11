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

@Api(value = "/map", description = "Endpoint to Map Service")
@Path("/map")
public class MapService {

    private MapManager mapManager;
    private final static Logger logger = Logger.getLogger(MapService.class);

    public MapService() {
        this.mapManager = MapManagerImpl.getInstance();
    }

    private <T> Response createListResponse(List<T> list, String emptyMessage) {
        if (list == null || list.isEmpty()) {
            return Response.status(404).entity(emptyMessage).build();
        }
        GenericEntity<List<T>> entity = new GenericEntity<List<T>>(list) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "Add a new user", notes = "Create a user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User Created", response = User.class),
            @ApiResponse(code = 400, message = "Invalid user details")
    })
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        if (user.getId() == null || user.getName() == null) {
            return Response.status(400).entity("Invalid user details").build();
        }
        try {
            this.mapManager.addUser(user);
            return Response.status(201).entity(user).build();
        } catch (Exception e) {
            logger.error("Error al añadir el usuario", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @GET
    @ApiOperation(value = "List users alphabetically", notes = "Retrieve all users ordered by last name and first name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = User.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "No users found")
    })
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers() {
        try {
            List<User> users = this.mapManager.getAllUsers();
            return createListResponse(users, "No users found");
        } catch (Exception e) {
            logger.error("Error al listar usuarios", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @GET
    @ApiOperation(value = "Get user details", notes = "Retrieve user information by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = User.class),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") String userId) {
        try {
            User user = this.mapManager.getUser(userId);
            if (user == null) return Response.status(404).entity("User not found").build();
            return Response.status(200).entity(user).build();
        } catch (Exception e) {
            logger.error("Error al obtener información del usuario", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @POST
    @ApiOperation(value = "Add interest point", notes = "Add a point of interest with specified coordinates and type")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Interest Point Created", response = InterestPoint.class),
            @ApiResponse(code = 400, message = "Invalid point of interest details")
    })
    @Path("/interestPoints")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addInterestPoint(InterestPoint point) {
        if (point.getX() < 0 || point.getY() < 0 || point.getType() == null) {
            return Response.status(400).entity("Invalid point of interest details").build();
        }
        try {
            this.mapManager.addInterestPoint(point);
            return Response.status(201).entity(point).build();
        } catch (Exception e) {
            logger.error("Error al añadir el punto de interés", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @POST
    @ApiOperation(value = "Record visit", notes = "Record a user's visit to a specified interest point")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Visit Recorded"),
            @ApiResponse(code = 404, message = "User or Interest Point not found")
    })
    @Path("/users/{userId}/visits")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recordVisit(@PathParam("userId") String userId, VisitRecord visit) {
        try {
            this.mapManager.recordVisit(userId, visit.getX(), visit.getY());
            return Response.status(201).entity("Visit Recorded").build();
        } catch (Exception e) {
            logger.error("Error al registrar la visita", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @GET
    @ApiOperation(value = "Get visited points", notes = "Get points of interest a user has visited")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = InterestPoint.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "No visits found for this user")
    })
    @Path("/users/{userId}/visits")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVisitedPoints(@PathParam("userId") String userId) {
        try {
            List<InterestPoint> points = this.mapManager.getVisitedPoints(userId);
            return createListResponse(points, "No visits found for this user");
        } catch (Exception e) {
            logger.error("Error al obtener puntos de interés visitados", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }

    @GET
    @ApiOperation(value = "Get users by interest point", notes = "List users who have visited a specific point of interest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = User.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "No users found for this point of interest")
    })
    @Path("/interestPoints/{x}/{y}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersByInterestPoint(@PathParam("x") int x, @PathParam("y") int y) {
        try {
            List<User> users = this.mapManager.getUsersByInterestPoint(x, y);
            return createListResponse(users, "No users found for this point of interest");
        } catch (Exception e) {
            logger.error("Error al obtener usuarios por punto de interés", e);
            return Response.status(500).entity("Internal Server Error: " + e.getMessage()).build();
        }
    }
}

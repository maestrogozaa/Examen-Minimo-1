package edu.upc.dsa;

import edu.upc.dsa.models.InterestPoint;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.VisitRecord;
import edu.upc.dsa.models.ElementType;
import org.apache.log4j.Logger;

import java.util.*;

public class MapManagerImpl implements MapManager {
    private static MapManagerImpl instance;
    private Map<String, User> users;
    private List<InterestPoint> interestPoints;
    private List<VisitRecord> visitRecords;
    private final static Logger logger = Logger.getLogger(MapManagerImpl.class);

    MapManagerImpl() {
        users = new HashMap<>();
        interestPoints = new ArrayList<>();
        visitRecords = new ArrayList<>();
    }

    public static MapManagerImpl getInstance() {
        if (instance == null) {
            instance = new MapManagerImpl();
            logger.info("MapManagerImpl instance created.");
        }
        return instance;
    }

    @Override
    public void addUser(String id, String name, String surname, String email, Date birthDate) {
        logger.info("addUser() called with: id=" + id + ", name=" + name + ", surname=" + surname);
        if (id == null || name == null || surname == null || email == null || birthDate == null) {
            logger.error("Invalid parameters: one or more parameters are null.");
            return;
        }
        User user = new User(id, name, surname, email, birthDate);
        users.put(id, user);
    }

    @Override
    public List<User> getAllUsersSorted() {
        logger.info("getAllUsersSorted() called.");
        List<User> sortedUsers = new ArrayList<>(users.values());
        sortedUsers.sort(Comparator.comparing(User::getSurname).thenComparing(User::getName));
        return sortedUsers;
    }

    @Override
    public User getUserById(String userId) {
        logger.info("getUserById() called with userId=" + userId);
        User user = users.get(userId);
        if (user == null) {
            logger.error("User not found: " + userId);
        }
        return user;
    }

    @Override
    public void addInterestPoint(int x, int y, ElementType type) {
        // Crear un nuevo InterestPoint con los parámetros recibidos
        InterestPoint point = new InterestPoint(x, y, new Date(), type);
        // Añadir el InterestPoint a la lista de puntos de interés
        interestPoints.add(point);
        logger.info("InterestPoint added at (" + x + ", " + y + ") with type: " + type);
    }

    @Override
    public void recordVisit(String userId, int x, int y) {
        logger.info("recordVisit() called with userId=" + userId + ", x=" + x + ", y=" + y);
        if (!users.containsKey(userId)) {
            logger.error("User not found: " + userId);
            return;
        }

        Optional<InterestPoint> point = interestPoints.stream()
                .filter(p -> p.getX() == x && p.getY() == y).findFirst();

        if (!point.isPresent()) { // Cambié isEmpty() por !isPresent()
            logger.error("Interest point not found at coordinates: " + x + ", " + y);
            return;
        }

        visitRecords.add(new VisitRecord(userId, x, y, new Date())); // Agregado Date para la fecha de visita
        logger.info("Visit recorded for userId=" + userId + " at coordinates (" + x + ", " + y + ")");
    }

    @Override
    public List<InterestPoint> getUserVisits(String userId) {
        logger.info("getUserVisits() called for userId=" + userId);
        List<InterestPoint> visits = new ArrayList<>();
        for (VisitRecord record : visitRecords) {
            if (record.getUserId().equals(userId)) {
                // Asumiendo que InterestPoint usa las coordenadas y la fecha en su constructor
                visits.add(new InterestPoint(record.getX(), record.getY(), record.getTimestamp(), ElementType.DOOR)); // El tipo se asume como PARK
            }
        }
        return visits;
    }

    @Override
    public void addInterestPoint(InterestPoint point) {
        logger.info("addInterestPoint() called with: point=" + point);
        if (point == null) {
            logger.error("InterestPoint cannot be null.");
            return;
        }
        interestPoints.add(point);
    }

    @Override
    public List<User> getUsersByInterestPoint(int x, int y) {
        logger.info("getUsersByInterestPoint() called with x=" + x + ", y=" + y);
        List<User> usersAtPoint = new ArrayList<>();
        for (VisitRecord record : visitRecords) {
            if (record.getX() == x && record.getY() == y) {
                usersAtPoint.add(users.get(record.getUserId()));
            }
        }
        return usersAtPoint;
    }

    @Override
    public List<InterestPoint> getInterestPointsByType(ElementType type) {
        logger.info("getInterestPointsByType() called with type=" + type);
        List<InterestPoint> pointsOfType = new ArrayList<>();
        for (InterestPoint point : interestPoints) {
            if (point.getType() == type) {
                pointsOfType.add(point);
            }
        }
        return pointsOfType;
    }

    @Override
    public void clearData() {
        logger.info("clearData() called.");
        users.clear();
        interestPoints.clear();
        visitRecords.clear();
    }

    // Nuevos métodos añadidos para cumplir con la interfaz MapManager
    public void addUser(User user) {
        logger.info("addUser() called with: user=" + user);
        if (user == null || user.getId() == null) {
            logger.error("User or userId cannot be null.");
            return;
        }
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("getAllUsers() called.");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(String userId) {
        logger.info("getUser() called with userId=" + userId);
        return users.get(userId);
    }

    @Override
    public List<InterestPoint> getVisitedPoints(String userId) {
        logger.info("getVisitedPoints() called for userId=" + userId);
        List<InterestPoint> visitedPoints = new ArrayList<>();
        for (VisitRecord record : visitRecords) {
            if (record.getUserId().equals(userId)) {
                visitedPoints.add(new InterestPoint(record.getX(), record.getY(), record.getTimestamp(), ElementType.DOOR));
            }
        }
        return visitedPoints;
    }
}

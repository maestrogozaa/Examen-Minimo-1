package edu.upc.dsa;

import edu.upc.dsa.models.InterestPoint;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.ElementType;
import java.util.Date;
import java.util.List;

public interface MapManager {
    // Métodos para usuarios
    void addUser(String id, String name, String surname, String email, Date birthDate); // Definir cómo agregar un usuario
    List<User> getAllUsersSorted(); // Obtener todos los usuarios ordenados
    User getUserById(String userId); // Obtener un usuario por ID

    // Métodos para puntos de interés

    void addInterestPoint(int x, int y, ElementType type); // Agregar un punto de interés
    void recordVisit(String userId, int x, int y); // Registrar visita a un punto de interés
    List<InterestPoint> getUserVisits(String userId); // Obtener visitas de un usuario
    List<User> getUsersByInterestPoint(int x, int y); // Obtener usuarios que visitaron un punto de interés
    List<InterestPoint> getInterestPointsByType(ElementType type); // Obtener puntos de interés por tipo

    // Métodos adicionales
    void addInterestPoint(InterestPoint point);
    void addUser(User user);

    void clearData(); // Limpiar datos
    List<User> getAllUsers(); // Obtener todos los usuarios
    User getUser(String userId); // Obtener un usuario
    List<InterestPoint> getVisitedPoints(String userId); // Obtener puntos de interés visitados por un usuario
}

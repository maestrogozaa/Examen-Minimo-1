package edu.upc.dsa;

import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.InterestPoint;
import edu.upc.dsa.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MapManagerTest {

    private MapManager manager;

    @BeforeEach
    public void setUp() {
        // Crear una instancia de MapManager (puedes usar un mock si es necesario)
        manager = new MapManagerImpl(); // Asegúrate de usar la implementación correcta
    }

    // Test para añadir un usuario
    @Test
    public void testAddUser() {
        String id = "1";
        String name = "Alice";
        String surname = "Adams";
        String email = "alice@example.com";
        Date birthDate = new Date();

        manager.addUser(id, name, surname, email, birthDate);

        User user = manager.getUser(id);
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(surname, user.getSurname());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetAllUsersSorted() {
        // Asegúrate de que manager esté inicializado
        MapManagerImpl manager = new MapManagerImpl();

        // Agregar usuarios
        manager.addUser("1", "Alice", "Smith", "alice@example.com", new Date());
        manager.addUser("2", "Bob", "Jones", "bob@example.com", new Date());

        // Obtener los usuarios ordenados
        List<User> users = manager.getAllUsersSorted();

        // Verificar que la lista no esté vacía
        assertEquals(2, users.size());  // Verifica que haya dos usuarios
        assertEquals("Bob", users.get(0).getName());  // Bob debe ser el primero
        assertEquals("Alice", users.get(1).getName());  // Alice debe ser el segundo
    }

    // Test para consultar un usuario por su ID
    @Test
    public void testGetUserById() {
        manager.addUser("1", "Alice", "Adams", "alice@example.com", new Date());

        User user = manager.getUserById("1");

        assertNotNull(user);
        assertEquals("Alice", user.getName());
        assertEquals("Adams", user.getSurname());
    }

    // Test para añadir un punto de interés
    @Test
    public void testAddInterestPoint() {
        int x = 10;
        int y = 20;
        ElementType type = ElementType.TREE;

        manager.addInterestPoint(x, y, type);

        List<InterestPoint> points = manager.getInterestPointsByType(ElementType.TREE);
        assertEquals(1, points.size());
        assertEquals(x, points.get(0).getX());
        assertEquals(y, points.get(0).getY());
    }

    // Test para registrar que un usuario pasa por un punto de interés
    @Test
    public void testRecordVisit() {
        manager.addUser("1", "Alice", "Adams", "alice@example.com", new Date());
        manager.addInterestPoint(10, 20, ElementType.TREE);

        manager.recordVisit("1", 10, 20);

        List<InterestPoint> visits = manager.getUserVisits("1");
        assertEquals(1, visits.size());
        assertEquals(10, visits.get(0).getX());
        assertEquals(20, visits.get(0).getY());
    }

    // Test para consultar los puntos de interés por los que un usuario ha pasado
    @Test
    public void testGetUserVisits() {
        manager.addUser("1", "Alice", "Adams", "alice@example.com", new Date());
        manager.addInterestPoint(10, 20, ElementType.TREE);
        manager.addInterestPoint(15, 25, ElementType.POTION);

        manager.recordVisit("1", 10, 20);
        manager.recordVisit("1", 15, 25);

        List<InterestPoint> visits = manager.getUserVisits("1");

        assertEquals(2, visits.size());
        assertEquals(10, visits.get(0).getX());
        assertEquals(20, visits.get(0).getY());
        assertEquals(15, visits.get(1).getX());
        assertEquals(25, visits.get(1).getY());
    }

    // Test para listar los usuarios que han pasado por un punto de interés
    @Test
    public void testGetUsersByInterestPoint() {
        manager.addUser("1", "Alice", "Adams", "alice@example.com", new Date());
        manager.addUser("2", "Bob", "Brown", "bob@example.com", new Date());
        manager.addInterestPoint(10, 20, ElementType.TREE);

        manager.recordVisit("1", 10, 20);
        manager.recordVisit("2", 10, 20);

        List<User> users = manager.getUsersByInterestPoint(10, 20);

        assertEquals(2, users.size());
        assertEquals("Alice", users.get(0).getName());
        assertEquals("Bob", users.get(1).getName());
    }

    // Test para consultar los puntos de interés de un tipo determinado
    @Test
    public void testGetInterestPointsByType() {
        manager.addInterestPoint(10, 20, ElementType.TREE);
        manager.addInterestPoint(15, 25, ElementType.TREE);
        manager.addInterestPoint(30, 35, ElementType.POTION);

        List<InterestPoint> points = manager.getInterestPointsByType(ElementType.TREE);

        assertEquals(2, points.size());
        assertEquals(ElementType.TREE, points.get(0).getType());
        assertEquals(ElementType.TREE, points.get(1).getType());
    }
}

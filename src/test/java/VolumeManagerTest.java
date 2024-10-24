import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import managers.VolumeManager;
import model.Book;
import model.Volume;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.VolumeRepo;

import static org.junit.jupiter.api.Assertions.*;

public class VolumeManagerTest {

    private static VolumeRepo volumeRepo;
    private static VolumeManager volumeManager;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        volumeRepo = new VolumeRepo(entityManager);
        volumeManager = new VolumeManager(volumeRepo);
    }

    @Test
    void testAddVolumeWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new VolumeManager(null);
        });

        assertEquals("volumeRepo cannot be null", exception.getMessage());
    }

    @Test
    void testAddVolume() {
        Book book = new Book("Stanislaw Lem", "Bajki robotow", "Science Fiction");
        volumeManager.addVolume(book);
        Volume addedVolume = volumeRepo.get(book.getVolumeId());
        assertEquals(book, addedVolume);
    }
//
//    @Test
//    void testRegisterVehicleWithExistingId() {
//        Car car = new Car("ABC123", "Toyota", 100, 'B', 1.8);
//        vehicleRepository.add(car);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            vehicleManager.registerVehicle(car);
//        });
//
//        assertEquals("Vehicle with the same ID already exists.", exception.getMessage());
//    }
//
//    @Test
//    void testUnregisterVehicle() {
//        Car car = new Car("XYZ789", "Honda", 150, 'C', 2.0);
//        vehicleRepository.add(car);
//
//        vehicleManager.unregisterVehicle(car);
//
//        Vehicle unregisteredVehicle = vehicleRepository.get(car.getVehicleId());
//        assertTrue(unregisteredVehicle.isArchived());
//        assertFalse(unregisteredVehicle.isAvailable());
//    }
}
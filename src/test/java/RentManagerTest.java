//import managers.RentManager;
//import model.NoCard;
//import model.Rent;
//import model.Renter;
//import model.Volume;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import repositories.RentRepo;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RentManagerTest {
//
//    private RentManager rentManager;
//    private Rent rent;
//    private Volume volume1;
//    private Volume volume2;
//
//    @BeforeEach
//    void setUp() {
//        rentManager = new RentManager();
//
//        // Create a Renter and Volumes
//        Renter renter = new Renter("John", "Doe", "12345", new NoCard());
//        UUID rentId = UUID.randomUUID();
//
//        // Initialize Rent with a temporary volume to avoid ParameterException
//        rent = new Rent(rentId, renter, new Volume("TempVolume", "Temporary"), LocalDateTime.now());
//
//        volume1 = new Volume("Volume1", "Fantasy Book");
//        volume2 = new Volume("Volume2", "Science Fiction Book");
//    }
//
//    @Test
//    void testCreateRentWithNoExistingRents() {
//        List<Volume> volumes = Arrays.asList(volume1); // Only one volume
//
//        boolean result = rentManager.createRent(volumes, rent);
//
//        assertTrue(result); // Should be successful since there are no existing rents
//        assertEquals(volume1, rent.getVolume()); // The rent should be associated with volume1
//        assertEquals(1, rentManager.countRents()); // Rent count should be 1
//    }
//
//    @Test
//    void testCreateRentWithExistingRents() {
//        // First, we add a rent for volume1
//        rentManager.createRent(Arrays.asList(volume1), rent);
//
//        // Now we try to create a new rent with the same volume
//        Rent newRent = new Rent(UUID.randomUUID(), new Renter("Jane", "Doe", "67890", new NoCard()), new Volume("TempVolume", "Temporary"), LocalDateTime.now());
//        boolean result = rentManager.createRent(Arrays.asList(volume1), newRent);
//
//        assertFalse(result); // Should fail since volume1 is already rented
//        assertNull(newRent.getVolume()); // The new rent should not be associated with any volume
//        assertEquals(1, rentManager.countRents()); // Rent count should still be 1
//    }
//
//    @Test
//    void testCountRents() {
//        // Initial count should be 0
//        assertEquals(0, rentManager.countRents());
//
//        // Add a rent
//        rentManager.createRent(Arrays.asList(volume1), rent);
//        assertEquals(1, rentManager.countRents());
//
//        // Add another rent
//        Rent newRent = new Rent(UUID.randomUUID(), new Renter("Jane", "Doe", "67890", new NoCard()), new Volume("TempVolume", "Temporary"), LocalDateTime.now());
//        rentManager.createRent(Arrays.asList(volume2), newRent);
//        assertEquals(2, rentManager.countRents());
//    }
//}

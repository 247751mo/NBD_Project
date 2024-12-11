import managers.RenterManager;
import model.Renter;
import org.junit.jupiter.api.*;
import repositories.MongoRenterRepo;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RenterManagerTest {

    private MongoRenterRepo mongoRenterRepo;
    private RenterManager renterManager;

    @BeforeAll
    void setupDatabase() {
        mongoRenterRepo = new MongoRenterRepo();
        mongoRenterRepo.initDbConnection();
        renterManager = new RenterManager(mongoRenterRepo);
    }

    @AfterAll
    void closeDatabase() {
        mongoRenterRepo.close();
    }

    @BeforeEach
    void cleanUp() {
        mongoRenterRepo.readAll().forEach(mongoRenterRepo::delete);
    }

    @Test
    @Order(1)
    void testRegisterRenter_whenRenterDoesNotExist_shouldRegisterSuccessfully() {
        Renter renter = new Renter("12345", "John", "Doe");

        renterManager.registerRenter(renter);

        Renter retrievedRenter = mongoRenterRepo.read("12345");
        assertNotNull(retrievedRenter);
        assertEquals("John", retrievedRenter.getFirstName());
        assertEquals("Doe", retrievedRenter.getLastName());
        assertFalse(retrievedRenter.isArchived());
        assertEquals(0, retrievedRenter.getCurrentRentsNumber());
    }

    @Test
    @Order(2)
    void testRegisterRenter_whenRenterAlreadyExists_shouldThrowException() {
        Renter renter = new Renter("12345", "John", "Doe");
        mongoRenterRepo.create(renter);

        Renter duplicateRenter = new Renter("12345", "Jane", "Smith");

        assertThrows(IllegalArgumentException.class, () -> renterManager.registerRenter(duplicateRenter));
    }

    @Test
    @Order(3)
    void testUnregisterRenter_shouldArchiveRenter() {
        Renter renter = new Renter("12345", "John", "Doe");
        mongoRenterRepo.create(renter);

        renterManager.unregisterRenter(renter);

        Renter updatedRenter = mongoRenterRepo.read("12345");
        assertNotNull(updatedRenter);
        assertTrue(updatedRenter.isArchived());
    }

    @Test
    @Order(4)
    void testUnregisterRenter_whenRenterIsNull_shouldDoNothing() {
        assertDoesNotThrow(() -> renterManager.unregisterRenter(null));
    }
}

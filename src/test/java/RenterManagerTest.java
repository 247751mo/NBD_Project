import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import managers.RenterManager;
import model.Renter;
import model.NoCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.RenterRepo;

import static org.junit.jupiter.api.Assertions.*;

public class RenterManagerTest {

    private static RenterRepo renterRepo;
    private static RenterManager renterManager;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        renterRepo = new RenterRepo(entityManager);
        renterManager = new RenterManager(renterRepo);
    }

    @Test
    void testRenterManagerWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RenterManager(null);
        });

        assertEquals("renterRepo cannot be null", exception.getMessage());
    }

    @Test
    void testAddRenterWithExistingId() {
        Renter renter = new Renter("Tyler", "Okonma", "TYLER1", new NoCard());
        renterRepo.add(renter);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            renterManager.addRenter(renter);
        });

        assertEquals("Renter already exists", exception.getMessage());
    }

    @Test
    void testAddRenterSuccessfully() {
        Renter renter = new Renter("Frank", "Ocean", "FRANK325", new NoCard());

        renterManager.addRenter(renter);

        Renter foundRenter = renterRepo.get(renter.getId());
        assertNotNull(foundRenter);
        assertEquals("Frank", foundRenter.getFirstName());
        assertEquals("Ocean", foundRenter.getLastName());
    }

    @Test
    void testRemoveRenter() {
        Renter renter = new Renter("Kali", "Uchis", "KALI343", new NoCard());
        renterRepo.add(renter);

        renterManager.removeRenter(renter);

        Renter archivedRenter = renterRepo.get(renter.getId());
        assertTrue(archivedRenter.checkIfArchived());
    }
}

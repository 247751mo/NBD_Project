import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Renter;
import model.RenterType;
import model.NoCard;
import model.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.RenterRepo;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class RenterRepoTest {

    private static RenterRepo renterRepo;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        renterRepo = new RenterRepo(entityManager);
    }

    @Test
    void testAddRenter() {
        RenterType noCardType = new NoCard();
        RenterType cardType = new Card();

        Renter renter1 = new Renter("Alice", "Johnson", "ALICE123", noCardType);
        Renter renter2 = new Renter("Bob", "Smith", "BOB456", cardType);

        renterRepo.add(renter1);
        renterRepo.add(renter2);

        Renter foundRenter1 = renterRepo.get(renter1.getId());
        Renter foundRenter2 = renterRepo.get(renter2.getId());

        assertNotNull(foundRenter1);
        assertNotNull(foundRenter2);
        assertEquals("Alice", foundRenter1.getFirstName());
        assertEquals("Bob", foundRenter2.getFirstName());
        assertEquals("No Card", foundRenter1.getType().getRenterTypeInfo());
        assertEquals("Card", foundRenter2.getType().getRenterTypeInfo());
    }

    @Test
    void testRemoveRenter() {
        RenterType noCardType = new NoCard();
        Renter renter = new Renter("John", "Rambo", "CHARLIE789", noCardType);
        renterRepo.add(renter);

        Renter foundRenter1 = renterRepo.get(renter.getId());
        assertNotNull(foundRenter1);

        renterRepo.delete(renter);

        Renter foundRenter2 = renterRepo.get(renter.getId());
        assertNull(foundRenter2);
    }

    @Test
    void testUpdateRenter() {
        RenterType noCardType = new NoCard();
        Renter renter = new Renter("David", "Lee", "DAVID101", noCardType);
        renterRepo.add(renter);

        Renter foundRenter1 = renterRepo.get(renter.getId());
        assertEquals("David", foundRenter1.getFirstName());
        assertEquals("Lee", foundRenter1.getLastName());
        assertEquals("DAVID101", foundRenter1.getPersonalID());
        assertEquals("No Card", foundRenter1.getType().getRenterTypeInfo());

        renter.setFirstName("Daniel");
        renter.setLastName("Brown");

        RenterType cardType = new Card();
        renter.setType(cardType);  // Changing renterType

        renterRepo.update(renter);

        Renter foundRenter2 = renterRepo.get(renter.getId());
        assertEquals("Daniel", foundRenter2.getFirstName());
        assertEquals("Brown", foundRenter2.getLastName());
        assertEquals("Card", foundRenter2.getType().getRenterTypeInfo());
    }

    @Test
    void testGetAllRenters() {
        List<Renter> renters = renterRepo.getAll();
        int initialSize = renters.size();

        RenterType noCardType = new NoCard();
        RenterType cardType = new Card();

        Renter renter1 = new Renter("Emma", "Davis", "EMMA123", noCardType);
        Renter renter2 = new Renter("Frank", "Taylor", "FRANK456", cardType);

        renterRepo.add(renter1);
        renterRepo.add(renter2);

        renters = renterRepo.getAll();
        int finalSize = renters.size();

        assertEquals(initialSize + 2, finalSize);
    }

    @Test
    void testArchiveRenter() {
        RenterType noCardType = new NoCard();
        Renter renter = new Renter("George", "Hall", "GEORGE789", noCardType);
        renterRepo.add(renter);

        renter.setArchiveStatus(true);
        renterRepo.update(renter);

        Renter foundRenter = renterRepo.get(renter.getId());
        assertTrue(foundRenter.checkIfArchived());
    }

    @Test
    void testMaxVolumes() {
        RenterType noCardType = new NoCard();
        RenterType cardType = new Card();

        Renter renter1 = new Renter("Hannah", "Walker", "HANNAH123", noCardType);
        Renter renter2 = new Renter("Ian", "Scott", "IAN456", cardType);

        assertEquals(5, renter1.maxVolumes(0));  // NoCard should allow max 5 volumes
        assertEquals(10, renter2.maxVolumes(0)); // Card should allow max 10 volumes

        renter1.setType(cardType);
        assertEquals(10, renter1.maxVolumes(0)); // Now Hannah has a card, should allow max 10 volumes
    }
}

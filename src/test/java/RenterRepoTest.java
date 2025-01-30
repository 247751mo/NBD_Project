import model.Renter;
import org.junit.jupiter.api.*;
import repositories.RenterRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class RenterRepoTest {

    private static RenterRepo renterRepo;

    @BeforeAll
    public static void setUp() {
        renterRepo = new RenterRepo();
    }

    @BeforeEach
    public void cleanUp() {
        renterRepo.getDatabase().getCollection("renters", Renter.class).drop();
    }

    @AfterAll
    public static void tearDown() {
        renterRepo.getDatabase().getCollection("renters", Renter.class).drop();
        renterRepo.close();
    }

    @Test
    void testCreateRenter() {


        Renter renter1 = new Renter("233", "Johnson", "Alice");
        Renter renter2 = new Renter("323", "Smith", "Bob");


        renterRepo.create(renter1);
        renterRepo.create(renter2);

        Renter foundRenter1 = renterRepo.read(renter1.getPersonalID());
        Renter foundRenter2 = renterRepo.read(renter2.getPersonalID());

        assertEquals(renter1, foundRenter1);
        assertEquals(renter2, foundRenter2);
    }

    @Test
    void testRemoveRenter() {

        Renter renter = new Renter("54623", "Big", "Ben");
        renterRepo.create(renter);
        Renter foundRenter = renterRepo.read(renter.getPersonalID());
        assertNotNull(foundRenter);

        renterRepo.delete(renter);
        Renter foundRenter1 = renterRepo.read(renter.getPersonalID());
        assertNull(foundRenter1);
    }

    @Test
    void testUpdateRenter() {
        Renter renter = new Renter("55437653", "Max", "Zly");
        renterRepo.create(renter);

        Renter foundRenter = renterRepo.read(renter.getPersonalID());
        assertEquals("55437653", foundRenter.getPersonalID());
        assertEquals("Max", foundRenter.getFirstName());
        assertEquals("Zly", foundRenter.getLastName());

        renter.setFirstName("Adam");
        renter.setLastName("Malysz");

        renterRepo.update(renter);

        Renter foundRenter1 = renterRepo.read(renter.getPersonalID());
        assertEquals("Adam", foundRenter1.getFirstName());
        assertEquals("Malysz", foundRenter1.getLastName());
        assertEquals("55437653", foundRenter.getPersonalID());
    }

    @Test
    void testReadAllRenters() {
        List<Renter> renters = renterRepo.readAll();
        int initialSize = renters.size();

        Renter renter1 = new Renter("233", "Johnson", "Alice");
        Renter renter2 = new Renter("323", "Smith", "Bob");

        renterRepo.create(renter1);
        renterRepo.create(renter2);

        renters = renterRepo.readAll();
        int finalsize = renters.size();

        assertEquals(initialSize +2, finalsize);
    }


}
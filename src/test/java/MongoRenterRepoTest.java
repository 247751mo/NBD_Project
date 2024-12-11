import model.Renter;
import org.junit.jupiter.api.*;
import repositories.MongoRenterRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class MongoRenterRepoTest {

    private static MongoRenterRepo mongoRenterRepo;

    @BeforeAll
    public static void setUp() {
        mongoRenterRepo = new MongoRenterRepo();
    }

    @BeforeEach
    public void cleanUp() {
        mongoRenterRepo.getDatabase().getCollection("renters", Renter.class).drop();
    }

    @AfterAll
    public static void tearDown() {
        mongoRenterRepo.getDatabase().getCollection("renters", Renter.class).drop();
        mongoRenterRepo.close();
    }

    @Test
    void testCreateRenter() {


        Renter renter1 = new Renter("233", "Johnson", "Alice");
        Renter renter2 = new Renter("323", "Smith", "Bob");


        mongoRenterRepo.create(renter1);
        mongoRenterRepo.create(renter2);

        Renter foundRenter1 = mongoRenterRepo.read(renter1.getPersonalID());
        Renter foundRenter2 = mongoRenterRepo.read(renter2.getPersonalID());

        assertEquals(renter1, foundRenter1);
        assertEquals(renter2, foundRenter2);
    }

    @Test
    void testRemoveRenter() {

        Renter renter = new Renter("54623", "Big", "Ben");
        mongoRenterRepo.create(renter);
        Renter foundRenter = mongoRenterRepo.read(renter.getPersonalID());
        assertNotNull(foundRenter);

        mongoRenterRepo.delete(renter);
        Renter foundRenter1 = mongoRenterRepo.read(renter.getPersonalID());
        assertNull(foundRenter1);
    }

    @Test
    void testUpdateRenter() {
        Renter renter = new Renter("55437653", "Max", "Zly");
        mongoRenterRepo.create(renter);

        Renter foundRenter = mongoRenterRepo.read(renter.getPersonalID());
        assertEquals("55437653", foundRenter.getPersonalID());
        assertEquals("Max", foundRenter.getFirstName());
        assertEquals("Zly", foundRenter.getLastName());

        renter.setFirstName("Adam");
        renter.setLastName("Malysz");

        mongoRenterRepo.update(renter);

        Renter foundRenter1 = mongoRenterRepo.read(renter.getPersonalID());
        assertEquals("Adam", foundRenter1.getFirstName());
        assertEquals("Malysz", foundRenter1.getLastName());
        assertEquals("55437653", foundRenter.getPersonalID());
    }

    @Test
    void testReadAllRenters() {
        List<Renter> renters = mongoRenterRepo.readAll();
        int initialSize = renters.size();

        Renter renter1 = new Renter("233", "Johnson", "Alice");
        Renter renter2 = new Renter("323", "Smith", "Bob");

        mongoRenterRepo.create(renter1);
        mongoRenterRepo.create(renter2);

        renters = mongoRenterRepo.readAll();
        int finalsize = renters.size();

        assertEquals(initialSize +2, finalsize);
    }


}
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import model.Card;
import model.NoCard;
import model.Renter;
import model.RenterType;
import org.bson.Document;
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
    void testAddRenter() {
        RenterType noCardType = new NoCard();
        RenterType cardType = new Card();

        Renter renter1 = new Renter("ALICE123", "Johnson", "Alice", noCardType);
        Renter renter2 = new Renter("BOB456", "Smith", "Bob", cardType);


        renterRepo.add(renter1);
        renterRepo.add(renter2);

        Renter foundClient1 = renterRepo.get(renter1.getPersonalID());
        Renter foundClient2 = renterRepo.get(renter2.getPersonalID());

        assertEquals(renter1, foundClient1);
        assertEquals(renter2, foundClient2);
    }
    @Test
    void testPersonalIdAsId() {
        Renter renter = new Renter("TEST123", "Test", "User", new NoCard());
        renterRepo.add(renter);

        MongoCollection<Document> collection = renterRepo.getDatabase().getCollection("renters");
        Document found = collection.find(Filters.eq("_id", "TEST123")).first();

        assertNotNull(found, "Renter not found in database");
        assertEquals("TEST123", found.getString("_id"));
    }


}

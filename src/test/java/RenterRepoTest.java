import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import model.Card;
import model.NoCard;
import model.Renter;
import model.RenterType;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonDocumentWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
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


        Renter renter1 = new Renter("233", "Johnson", "Alice");
        Renter renter2 = new Renter("323", "Smith", "Bob");


        renterRepo.add(renter1);
        renterRepo.add(renter2);

        Renter foundRenter1 = renterRepo.get(renter1.getPersonalID());
        Renter foundRenter2 = renterRepo.get(renter2.getPersonalID());

        assertEquals(renter1, foundRenter1);
        assertEquals(renter2, foundRenter2);
    }

    @Test
    void testRemoveRenter() {

        Renter renter = new Renter("54623", "Big", "Ben");
        renterRepo.add(renter);
        Renter foundRenter = renterRepo.get(renter.getPersonalID());
        assertNotNull(foundRenter);

        renterRepo.delete(renter);
        Renter foundRenter1 = renterRepo.get(renter.getPersonalID());
        assertNull(foundRenter1);
    }

    @Test
    void testUpdateRenter() {
        Renter renter = new Renter("55437653", "Max", "Zly");
        renterRepo.add(renter);

        Renter foundRenter = renterRepo.get(renter.getPersonalID());
        assertEquals("55437653", foundRenter.getPersonalID());
        assertEquals("Max", foundRenter.getFirstName());
        assertEquals("Zly", foundRenter.getLastName());

        renter.setFirstName("Adam");
        renter.setLastName("Malysz");

        renterRepo.update(renter);

        Renter foundRenter1 = renterRepo.get(renter.getPersonalID());
        assertEquals("Adam", foundRenter1.getFirstName());
        assertEquals("Malysz", foundRenter1.getLastName());
        assertEquals("55437653", foundRenter.getPersonalID());
    }

    @Test
    void testGetAllRenters() {
        List<Renter> renters = renterRepo.getAll();
        int initialSize = renters.size();

        Renter renter1 = new Renter("233", "Johnson", "Alice");
        Renter renter2 = new Renter("323", "Smith", "Bob");

        renterRepo.add(renter1);
        renterRepo.add(renter2);

        renters = renterRepo.getAll();
        int finalsize = renters.size();

        assertEquals(initialSize +2, finalsize);
    }


}
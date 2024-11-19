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
        RenterType noCardType = new NoCard();
        RenterType cardType = new Card();

        Renter renter1 = new Renter("233", "Johnson", "Alice");
        Renter renter2 = new Renter("323", "Smith", "Bob");


        renterRepo.add(renter1);
        renterRepo.add(renter2);

        Renter foundClient1 = renterRepo.get(renter1.getPersonalID());
        Renter foundClient2 = renterRepo.get(renter2.getPersonalID());

        assertEquals(renter1, foundClient1);
        assertEquals(renter2, foundClient2);
    }
    @Test
    void testPersonalIdAsId() {
        Renter renter = new Renter("TEST123", "Test", "User");
        renterRepo.add(renter);

        MongoCollection<Document> collection = renterRepo.getDatabase().getCollection("renters");
        Document found = collection.find(Filters.eq("_id", "TEST123")).first();

        assertNotNull(found, "Renter not found in database");
        assertEquals("TEST123", found.getString("_id"));
    }

    @Test
    void testSerializationDeserialization() {
        RenterType cardType = new Card();
        Renter renter = new Renter("TEST123", "Doe", "John");

        renterRepo.add(renter);
        Renter retrievedRenter = renterRepo.get("TEST123");

        assertNotNull(retrievedRenter);
        assertEquals(renter.getPersonalID(), retrievedRenter.getPersonalID());

    }
    @Test
    void testPojoSerialization() {
        PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .automatic(true)
                .register(Renter.class, RenterType.class, Card.class, NoCard.class)
                .build();

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(pojoCodecProvider)
        );

        Codec<Renter> renterCodec = codecRegistry.get(Renter.class);

        RenterType cardType = new Card();
        Renter renter = new Renter("TEST123", "Doe", "John");

        // Serializacja
        Document document = new Document();
        BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
        EncoderContext encoderContext = EncoderContext.builder().isEncodingCollectibleDocument(true).build();
        renterCodec.encode(writer, renter, encoderContext);

        // Deserializacja
        BsonDocument bsonDocument = writer.getDocument();
        Renter decodedRenter = renterCodec.decode(
                new BsonDocumentReader(bsonDocument),
                DecoderContext.builder().build()
        );

        assertEquals(renter.getPersonalID(), decodedRenter.getPersonalID());

    }

}
